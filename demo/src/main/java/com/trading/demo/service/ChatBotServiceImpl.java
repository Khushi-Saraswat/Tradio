package com.trading.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.trading.demo.model.CoinDTO;
import com.trading.demo.response.ApiResponse;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    private static final String BASE_URL = "https://api.coingecko.com/api/v3/coins/";

    private final RestTemplate restTemplate = new RestTemplate();

    private double convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else {
            throw new IllegalArgumentException("Invalid type for conversion: " + value);
        }
    }

    private CoinDTO makeApiRequest(String coinId) {
        String url = BASE_URL + coinId.toLowerCase().trim();
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);

        Map<String, Object> responseBody = responseEntity.getBody();
        if (responseBody == null) {
            throw new RuntimeException("Invalid response from CoinGecko API");
        }

        Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");

        CoinDTO coinInfo = new CoinDTO();
        coinInfo.setId((String) responseBody.get("id"));
        coinInfo.setName((String) responseBody.get("name"));
        coinInfo.setSymbol((String) responseBody.get("symbol"));
        coinInfo.setCurrentPrice(convertToDouble(((Map<String, Object>) marketData.get("current_price")).get("inr")));
        coinInfo.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap")).get("inr")));
        coinInfo.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).get("inr")));
        coinInfo.setMarketCapRank((Integer) responseBody.get("market_cap_rank"));
        coinInfo.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("inr")));
        coinInfo.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("inr")));
        coinInfo.setCirculatingSupply(convertToDouble(marketData.get("circulating_supply")));
        coinInfo.setTotalSupply(convertToDouble(marketData.get("total_supply")));

        return coinInfo;
    }

    private String extractCoinIdViaSearch(String prompt) {
        String[] words = prompt.replaceAll("[^A-Za-z ]", "")
                .trim()
                .split("\\s+");
        String keyword = words[words.length - 1].toLowerCase();

        // CoinGecko uses ids like "bitcoin", "solana", etc.
        return keyword;
    }

    @Override
    public ApiResponse getCoinDetails(String prompt) {
        String coinId = extractCoinIdViaSearch(prompt);
        CoinDTO coinDetails = makeApiRequest(coinId);

        ApiResponse ans = new ApiResponse();
        String lowerPrompt = prompt.toLowerCase();
        String result;

        if (lowerPrompt.contains("price") || lowerPrompt.contains("value")) {
            result = "Current price of " + coinDetails.getName() + " is ₹" + coinDetails.getCurrentPrice();
        } else if (lowerPrompt.contains("market cap")) {
            result = "Market cap of " + coinDetails.getName() + " is ₹" + coinDetails.getMarketCap();
        } else if (lowerPrompt.contains("volume")) {
            result = "24h volume of " + coinDetails.getName() + " is ₹" + coinDetails.getTotalVolume();
        } else if (lowerPrompt.contains("rank")) {
            result = coinDetails.getName() + " is ranked #" + coinDetails.getMarketCapRank();
        } else if (lowerPrompt.contains("supply")) {
            result = "Circulating supply of " + coinDetails.getName() + " is " + coinDetails.getCirculatingSupply();
        } else {
            result = "Sorry, I couldn’t understand your query. Here's full info:\n" + coinDetails.toString();
        }

        ans.setMessage(result);
        return ans;
    }
}
