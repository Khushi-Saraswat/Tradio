package com.trading.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.demo.model.Coin;

@Service
public class CoinGeckoService implements CoinService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String loadMockJson(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("mockdata/" + fileName);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Mock file not found: " + fileName);
            return "[]";
        }
    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/" + coinId
                + "/market_chart?vs_currency=usd&days=" + days;

        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("API failed for " + coinId + " chart, using mock data...");
            String mockFile = "market_chart_" + coinId + ".json";
            return loadMockJson(mockFile);
        }
    }

    @Override
    public String getCoinDetails(String coinId) throws JsonProcessingException {

        String url = "https://api.coingecko.com/api/v3/coins/" + coinId;
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("API failed for coin details " + coinId + ", using top50/mock data...");

            String mockResponse = loadMockJson("top50.json");

            List<Map<String, Object>> coins = new ObjectMapper().readValue(mockResponse, List.class);
            for (Map<String, Object> coin : coins) {
                if (coinId.equals(coin.get("id"))) {
                    return new ObjectMapper().writeValueAsString(coin);
                }
            }

            return "{}";
        }
    }

    @Override
    public Coin findById(String coinId) throws Exception {
        String response = getCoinDetails(coinId);
        try {
            JsonNode node = objectMapper.readTree(response);
            Coin coin = new Coin();
            coin.setId(node.get("id").asText());
            coin.setSymbol(node.get("symbol").asText());
            coin.setName(node.get("name").asText());
            return coin;
        } catch (Exception e) {
            throw new Exception("Invalid coin data for: " + coinId, e);
        }
    }

    @Override
    public List<Coin> getCoinList(int page) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        try {
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(response, new TypeReference<List<Coin>>() {
            });
        } catch (Exception e) {
            System.err.println("API failed for coin list page " + page + ", using mock data...");
            String mockFile = "coinlist_page_" + page + ".json";
            String mockResponse = loadMockJson(mockFile);
            return objectMapper.readValue(mockResponse, new TypeReference<List<Coin>>() {
            });
        }
    }

    @Override
    public String getTop50CoinsByMarketCapRank() {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&page=1&per_page=50";
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("API failed for top 50 coins, using mock data...");
            return loadMockJson("top50.json");
        }
    }

    @Override
    public String getTreadingCoins() {
        String url = "https://api.coingecko.com/api/v3/search/trending";
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("API failed for trending coins, using mock data...");
            return loadMockJson("trending.json");
        }
    }

    @Override
    public String searchCoin(String keyword) {
        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            System.err.println("API failed for search: " + keyword + ", using mock data...");

            String mockResponse = loadMockJson("top50.json");
            List<Map<String, Object>> coins;
            try {
                coins = new ObjectMapper().readValue(mockResponse, List.class);
            } catch (Exception ex) {
                return "[]"; // failed to parse mock JSON
            }

            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> coin : coins) {
                String id = (String) coin.get("id");
                String name = (String) coin.get("name");
                String symbol = (String) coin.get("symbol");

                if ((id != null && id.toLowerCase().contains(keyword.toLowerCase()))
                        || (name != null && name.toLowerCase().contains(keyword.toLowerCase()))
                        || (symbol != null && symbol.toLowerCase().contains(keyword.toLowerCase()))) {
                    result.add(coin);
                }
            }

            try {
                return new ObjectMapper().writeValueAsString(result);
            } catch (Exception ex) {
                return "[]";
            }
        }
    }

}
