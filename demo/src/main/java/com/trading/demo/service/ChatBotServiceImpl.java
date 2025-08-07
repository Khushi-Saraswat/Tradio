package com.trading.demo.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.trading.demo.model.CoinDTO;
import com.trading.demo.response.ApiResponse;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    private static final String BASE_URL = "https://api.coincap.io/v2/assets/";

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
        if (responseBody == null || !responseBody.containsKey("data")) {
            throw new RuntimeException("Invalid response from CoinCap API");
        }

        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        CoinDTO coinInfo = new CoinDTO();
        coinInfo.setId((String) data.get("id"));
        coinInfo.setName((String) data.get("name"));
        coinInfo.setSymbol((String) data.get("symbol"));
        coinInfo.setCurrentPrice(convertToDouble(data.get("priceUsd")));
        coinInfo.setMarketCap(convertToDouble(data.get("marketCapUsd")));
        coinInfo.setTotalVolume(convertToDouble(data.get("volumeUsd24Hr")));
        coinInfo.setMarketCapRank(Integer.parseInt((String) data.get("rank")));
        coinInfo.setHigh24h(0); // Not provided by CoinCap
        coinInfo.setLow24h(0); // Not provided by CoinCap
        coinInfo.setCirculatingSupply(convertToDouble(data.get("supply")));
        coinInfo.setTotalSupply(convertToDouble(data.get("maxSupply") != null ? data.get("maxSupply") : "0"));

        return coinInfo;
    }

    private String extractCoinIdViaSearch(String prompt) {
        String[] words = prompt.replaceAll("[^A-Za-z ]", "")
                .trim()
                .split("\\s+");
        String keyword = words[words.length - 1].toLowerCase();

        // In CoinCap, ID is often the lower-case name e.g., "bitcoin", "solana", etc.
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
