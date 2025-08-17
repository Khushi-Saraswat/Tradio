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

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days=" + days;
        return fetchFromApi(url);
    }

    private double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getName());
        }
    }

    @Override
    public String getCoinDetails(String coinId) throws JsonProcessingException {

        Optional<Coin> coincache = coinRepository.findById(coinId);
        if (coincache.isPresent()) {
            return objectMapper.writeValueAsString(coincache.get());
        }

        String baseUrl = "https://api.coingecko.com/api/v3/coins/" + coinId;

        String response = fetchFromApi(baseUrl);
        // Coin coins = objectMapper.readValue(response.getBody(), new TypeReference<>()
        // {
        // });
        // coinRepository.save(coins);
        JsonNode jsonnode = objectMapper.readTree(response);
        Coin coin = mapJsonToCoin(jsonnode);
        coinRepository.save(coin);
        return response;

    }

    private Coin mapJsonToCoin(JsonNode jsonNode) {
        Coin coin = new Coin();

        System.out.println("id: " + jsonNode.get("id").asText());
        coin.setId(jsonNode.get("id").asText());

        System.out.println("symbol: " + jsonNode.get("symbol").asText());
        coin.setSymbol(jsonNode.get("symbol").asText());

        System.out.println("name: " + jsonNode.get("name").asText());
        coin.setName(jsonNode.get("name").asText());

        System.out.println("image: " + jsonNode.get("image").get("large").asText());
        coin.setImage(jsonNode.get("image").get("large").asText());

        JsonNode marketData = jsonNode.get("market_data");

        System.out.println("currentPrice: " + marketData.get("current_price").get("usd").asDouble());
        coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());

        System.out.println("marketCap: " + marketData.get("market_cap").get("usd").asLong());
        coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());

        System.out.println("marketCapRank: " + jsonNode.get("market_cap_rank").asInt());
        coin.setMarketCapRank(jsonNode.get("market_cap_rank").asInt());

        System.out.println("totalVolume: " + marketData.get("total_volume").get("usd").asLong());
        coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());

        System.out.println("high24h: " + marketData.get("high_24h").get("usd").asDouble());
        coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());

        System.out.println("low24h: " + marketData.get("low_24h").get("usd").asDouble());
        coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());

        System.out.println("priceChange24h: " + marketData.get("price_change_24h").asDouble());
        coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());

        System.out.println("priceChangePercentage24h: " + marketData.get("price_change_percentage_24h").asDouble());
        coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());

        System.out.println("marketCapChange24h: " + marketData.get("market_cap_change_24h").asLong());
        coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());

        System.out.println(
                "marketCapChangePercentage24h: " + marketData.get("market_cap_change_percentage_24h").asDouble());
        coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asDouble());

        System.out.println("circulatingSupply: " + marketData.get("circulating_supply").asLong());
        coin.setCirculatingSupply(marketData.get("circulating_supply").asLong());

        System.out.println("totalSupply: " + marketData.get("total_supply").asLong());
        coin.setTotalSupply(marketData.get("total_supply").asLong());

        return coin;

    }

    @Override
    public Coin findById(String coinId) throws Exception {
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if (optionalCoin.isEmpty())
            throw new Exception("invalid coin id");
        return optionalCoin.get();
    }

    @Override
    public String searchCoin(String keyword) {
        String baseUrl = "https://api.coingecko.com/api/v3/search?query=" + keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-demo-api-key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());

        return response.getBody();
    }

    @Override
    public String getTop50CoinsByMarketCapRank() {

        if (Top50Coins != null)
            return Top50Coins;

        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&page=1&per_page=50";
        String response = fetchFromApi(url);

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

    @Override
    public String getTreadingCoins() {

        if (trendingCache != null)
            return trendingCache;

        String url = "https://api.coingecko.com/api/v3/search/trending";
        trendingCache = fetchFromApi(url);
        return trendingCache;

    }

    @Override
    public List<Coin> getCoinList(int page) throws Exception {

        if (coinListCache.containsKey(page)) {
            return coinListCache.get(page);
        }
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        String response = fetchFromApi(url);

        try {
            // convert json into java object
            List<Coin> coins = objectMapper.readValue(response, new TypeReference<List<Coin>>() {
            });
            coinListCache.put(page, coins);
            return coins;
        } catch (Exception e) {
            throw new Exception("Error parsing coin list", e);
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

    @Scheduled(fixedRate = 300000)
    public void refreshesCache() {
        trendingCache = fetchFromApi("https://api.coingecko.com/api/v3/search/trending");
        Top50Coins = fetchFromApi("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&page=1&per_page=50");
        coinListCache.clear();
        System.out.println("schedular caches refreshes");

    }

}
