package com.trading.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trading.demo.model.Coin;

@Service
public class CoinServiceImpl implements CoinService {

    private final String BASE_URL = "https://api.coincap.io/v2/";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CoinServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<Coin> getCoinList(int page) throws Exception {
        int limit = 50;
        int offset = (page - 1) * limit;
        String url = BASE_URL + "assets?limit=" + limit + "&offset=" + offset;

        String response = restTemplate.getForObject(url, String.class);
        JsonNode jsonNode = objectMapper.readTree(response).get("data");

        List<Coin> coins = new ArrayList<>();
        for (JsonNode node : jsonNode) {
            Coin coin = new Coin();
            coin.setId(node.get("id").asText());
            coin.setName(node.get("name").asText());
            coin.setSymbol(node.get("symbol").asText());
            coin.setCurrentPrice(Double.parseDouble(node.get("priceUsd").asText()));
            coins.add(coin);
        }

        return coins;
    }

    @Override
    public String searchCoin(String keyword) throws Exception {
        String url = BASE_URL + "assets";
        String response = restTemplate.getForObject(url, String.class);
        JsonNode jsonNode = objectMapper.readTree(response).get("data");

        // Filter manually (CoinCap does not support query params like q=bitcoin)
        List<JsonNode> filtered = new ArrayList<>();
        for (JsonNode node : jsonNode) {
            String name = node.get("name").asText().toLowerCase();
            String symbol = node.get("symbol").asText().toLowerCase();
            if (name.contains(keyword.toLowerCase()) || symbol.contains(keyword.toLowerCase())) {
                filtered.add(node);
            }
        }

        return objectMapper.writeValueAsString(filtered);
    }

    @Override
    public String getTop50CoinsByMarketCapRank() throws Exception {
        String url = BASE_URL + "assets?limit=50";
        String response = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(response).get("data").toString();
    }

    @Override
    public String getTreadingCoins() throws Exception {
        // CoinCap doesn’t have a "trending" endpoint
        // Use top volume as "trending" alternative
        String url = BASE_URL + "assets?limit=10";
        String response = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(response).get("data").toString();
    }

    @Override
    public String getCoinDetails(String coinId) throws Exception {
        String url = BASE_URL + "assets/" + coinId;
        String response = restTemplate.getForObject(url, String.class);
        return objectMapper.readTree(response).get("data").toString();
    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        // CoinCap only provides 1d, 7d, 30d, 90d, 1y prices via /history
        String interval = "d1"; // daily
        String url = BASE_URL + "assets/" + coinId + "/history?interval=" + interval;
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @Override
    public Coin findById(String coinId) throws Exception {
        String url = BASE_URL + "assets/" + coinId;
        String json = restTemplate.getForObject(url, String.class);

        JsonNode jsonNode = objectMapper.readTree(json).get("data");

        Coin coin = new Coin();
        coin.setId(jsonNode.get("id").asText());
        coin.setName(jsonNode.get("name").asText());
        coin.setSymbol(jsonNode.get("symbol").asText());
        coin.setCurrentPrice(Double.parseDouble(jsonNode.get("priceUsd").asText()));

        return coin;
    }
}
