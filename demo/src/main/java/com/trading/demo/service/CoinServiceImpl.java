package com.trading.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.demo.model.Coin;
import com.trading.demo.repository.CoinRepository;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${coingecko.api.key}")
    private String API_KEY;

    private String trendingCache;
    private String Top50Coins;
    private Map<Integer, List<Coin>> coinListCache = new HashMap<>();

    // ------------------ MARKET CHART ------------------
    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
        return fetchFromApiSafe(url);
    }

    // ------------------ COIN DETAILS ------------------
    @Override
    public String getCoinDetails(String coinId) throws JsonProcessingException {

        Optional<Coin> coincache = coinRepository.findById(coinId);
        if (coincache.isPresent()) {
            return objectMapper.writeValueAsString(coincache.get());
        }

        String baseUrl = "https://api.coingecko.com/api/v3/coins/" + coinId;
        String response = fetchFromApiSafe(baseUrl);

        JsonNode jsonnode = objectMapper.readTree(response);
        Coin coin = mapJsonToCoin(jsonnode);
        coinRepository.save(coin);
        return response;
    }

    private Coin mapJsonToCoin(JsonNode jsonNode) {
        Coin coin = new Coin();

        coin.setId(jsonNode.get("id").asText());
        coin.setSymbol(jsonNode.get("symbol").asText());
        coin.setName(jsonNode.get("name").asText());
        coin.setImage(jsonNode.get("image").get("large").asText());

        JsonNode marketData = jsonNode.get("market_data");

        coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
        coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
        coin.setMarketCapRank(jsonNode.get("market_cap_rank").asInt());
        coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
        coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
        coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
        coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
        coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
        coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
        coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asDouble());
        coin.setCirculatingSupply(marketData.get("circulating_supply").asLong());
        coin.setTotalSupply(marketData.get("total_supply").asLong());

        return coin;
    }

    @Override
    public Coin findById(String coinId) throws Exception {
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if (optionalCoin.isEmpty())
            throw new Exception("Invalid coin id");
        return optionalCoin.get();
    }

    // ------------------ SEARCH COIN ------------------
    @Override
    public String searchCoin(String keyword) {
        String baseUrl = "https://api.coingecko.com/api/v3/search?query=" + keyword;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-demo-api-key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch (RuntimeException e) {
            System.out.println("Search API error: " + e.getMessage());
            return "[]"; // fallback empty data
        }
    }

    // ------------------ TOP 50 COINS ------------------
    @Override
    public String getTop50CoinsByMarketCapRank() {

        if (Top50Coins != null)
            return Top50Coins;

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&page=1&per_page=50";
        String response = fetchFromApiSafe(url);

        try {
            List<Coin> coins = objectMapper.readValue(response, new TypeReference<List<Coin>>() {
            });
            coinRepository.saveAll(coins);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Top50Coins = response;

        return response;
    }

    // ------------------ TRENDING COINS ------------------
    @Override
    public String getTreadingCoins() {
        if (trendingCache != null)
            return trendingCache;

        String url = "https://api.coingecko.com/api/v3/search/trending";
        trendingCache = fetchFromApiSafe(url);
        return trendingCache;
    }

    // ------------------ PAGINATED COIN LIST ------------------
    @Override
    public List<Coin> getCoinList(int page) throws Exception {

        if (coinListCache.containsKey(page)) {
            return coinListCache.get(page);
        }

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        String response = fetchFromApiSafe(url);

        try {
            List<Coin> coins = objectMapper.readValue(response, new TypeReference<List<Coin>>() {
            });
            coinListCache.put(page, coins);
            return coins;
        } catch (Exception e) {
            throw new Exception("Error parsing coin list", e);
        }
    }

    // ------------------ SAFE FETCH ------------------
    private String fetchFromApiSafe(String url) {
        try {
            return fetchFromApi(url);
        } catch (RuntimeException e) {
            System.out.println("API limit/error hit: " + e.getMessage() + ". Using cached data if available.");
            if (url.contains("trending") && trendingCache != null)
                return trendingCache;
            if (url.contains("markets") && Top50Coins != null)
                return Top50Coins;
            return "[]"; // fallback empty data
        }
    }

    private String fetchFromApi(String url) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-cg-demo-api-key", API_KEY);

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Free plan limit reached or API error: " + e.getMessage());
        }
    }

    // ------------------ SCHEDULER ------------------
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void refreshesCache() {
        try {
            if (trendingCache == null)
                trendingCache = "[]";
            if (Top50Coins == null)
                Top50Coins = "[]";

            trendingCache = fetchFromApiSafe("https://api.coingecko.com/api/v3/search/trending");
            Top50Coins = fetchFromApiSafe(
                    "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&page=1&per_page=50");

            // Coin list cache safely clear
            coinListCache.clear();

            System.out.println("Scheduler caches refreshed safely at " + java.time.LocalTime.now());
        } catch (Exception e) {
            // Exception ko completely catch kar diya, scheduler crash nahi hoga
            System.err.println("Scheduler error, using old cached data: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
