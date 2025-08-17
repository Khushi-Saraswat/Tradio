package com.trading.demo.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Service
public class CoinScheduler {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<JsonNode> coinListPage1 = new ArrayList<>();
    private JsonNode top50Coins;
    private JsonNode trendingCoins;
    private final Map<String, JsonNode> marketCharts = new HashMap<>();
    private final Map<String, JsonNode> coinDetails = new HashMap<>();
    private final Map<String, JsonNode> searchResults = new HashMap<>();

    public List<JsonNode> getCoinListPage1() {
        return coinListPage1;
    }

    public JsonNode getTop50Coins() {
        return top50Coins;
    }

    public JsonNode getTrendingCoins() {
        return trendingCoins;
    }

    public JsonNode getMarketChart(String coinId) {
        return marketCharts.get(coinId);
    }

    public JsonNode getCoinDetails(String coinId) {
        return coinDetails.get(coinId);
    }

    public JsonNode getSearchResults(String keyword) {
        return searchResults.get(keyword);
    }

    @PostConstruct
    public void init() {
        fetchCoinList();
        fetchTop50Coins();
        fetchTrendingCoins();
    }

    @Scheduled(fixedRate = 1000 * 60 * 15) // 15 minutes
    public void fetchCoinList() {
        try {
            String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&page=1&per_page=10";
            JsonNode response = objectMapper.readTree(restTemplate.getForObject(url, String.class));
            coinListPage1 = response.findValues(".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void fetchTop50Coins() {
        try {
            String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&page=1&per_page=50";
            top50Coins = objectMapper.readTree(restTemplate.getForObject(url, String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void fetchTrendingCoins() {
        try {
            String url = "https://api.coingecko.com/api/v3/search/trending";
            trendingCoins = objectMapper.readTree(restTemplate.getForObject(url, String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Optional: preload if you know IDs in advance
    public void fetchCoinDetails(String coinId) {
        try {
            String url = "https://api.coingecko.com/api/v3/coins/" + coinId;
            coinDetails.put(coinId, objectMapper.readTree(restTemplate.getForObject(url, String.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchMarketChart(String coinId, int days) {
        try {
            String url = "https://api.coingecko.com/api/v3/coins/" + coinId + "/market_chart?vs_currency=usd&days="
                    + days;
            marketCharts.put(coinId, objectMapper.readTree(restTemplate.getForObject(url, String.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchSearchCoin(String keyword) {
        try {
            String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
            searchResults.put(keyword, objectMapper.readTree(restTemplate.getForObject(url, String.class)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
