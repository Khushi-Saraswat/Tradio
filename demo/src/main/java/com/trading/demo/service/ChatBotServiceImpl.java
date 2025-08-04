package com.trading.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.trading.demo.model.CoinDTO;
import com.trading.demo.response.ApiResponse;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    private double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unsupported data type: " +
                    value.getClass().getName());
        }
    }

    public CoinDTO makeApiRequest(String currencyName) {
        // Implementation for making API request to fetch coin details

        System.out.println("Fetching details for: " + currencyName);
        String url = "https://api.coingecko.com/api/v3/coins/" + currencyName.toLowerCase().trim();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();
        if (responseBody != null) {

            Map<String, Object> image = (Map<String, Object>) responseBody.get("image");
            Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");
            CoinDTO coinInfo = new CoinDTO();
            coinInfo.setId((String) responseBody.get("id"));
            coinInfo.setSymbol((String) responseBody.get("symbol"));
            coinInfo.setName((String) responseBody.get("name"));
            coinInfo.setImage((String) image.get("large"));
            coinInfo.setCurrentPrice(
                    convertToDouble(((Map<String, Object>) marketData.get("current_price")).get("usd")));
            coinInfo.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap")).get("usd")));
            coinInfo.setMarketCapRank((int) responseBody.get("market_cap_rank"));
            coinInfo.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).get("usd")));
            coinInfo.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("usd")));
            coinInfo.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("usd")));
            coinInfo.setPriceChange24h(convertToDouble(marketData.get("price_change_24h")));
            coinInfo.setPriceChangePercentage24h(convertToDouble(marketData.get(
                    "price_change_percentage_24h")));
            coinInfo.setMarketCapChange24h(convertToDouble(marketData.get(
                    "market_cap_change_24h")));
            coinInfo.setMarketCapChangePercentage24h(
                    convertToDouble(marketData.get("market_cap_change_percentage_24h")));
            coinInfo.setCirculatingSupply(convertToDouble(marketData.get(
                    "circulating_supply")));
            coinInfo.setTotalSupply(convertToDouble(marketData.get("total_supply")));

            return coinInfo;
        }
        throw new RuntimeException("Failed to fetch coin details");

    }

    private String extractCoinIdViaSearch(String prompt) {
        // pick a simple keyword: e.g. the last word of the prompt
        String[] words = prompt.replaceAll("[^A-Za-z ]", "")
                .trim()
                .split("\\s+");
        String keyword = words[words.length - 1].toLowerCase();
        // System.out.println(Arrays.toString(words)); // ["What", "is", "the", "price",
        // "of", "solana"]
        System.out.println("Keyword: " + keyword); // solana

        String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
        /*
         * RestTemplate restTemplate = new RestTemplate();
         * HttpHeaders headers = new HttpHeaders();
         * 
         * HttpEntity<String> entity = new HttpEntity<>(headers);
         * ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url,
         * Map.class);
         * Map<String, Object> responseBody = responseEntity.getBody();
         * List<Map<String,Object>> coins = (List<Map<String,Object>>)
         * responseBody.getBody().get("coins");
         */
        RestTemplate rest = new RestTemplate();
        ResponseEntity<Map> resp = rest.getForEntity(url, Map.class);
        List<Map<String, Object>> coins = (List<Map<String, Object>>) resp.getBody().get("coins");
        System.out.println("Coins: " + coins);
        if (coins != null && !coins.isEmpty()) {
            return (String) coins.get(0).get("id");
        }
        throw new IllegalArgumentException("No matching coin for “" + keyword + "”");
    }

    @Override
    public ApiResponse getCoinDetails(String prompt) {

        String coinId = extractCoinIdViaSearch(prompt);
        CoinDTO coinDetails = makeApiRequest(coinId);
        System.out.println("Coin Details: " + coinDetails);
        // String functionResponse = getFunctionResponse(prompt).toString();
        // System.out.println("functionResponse: " + functionResponse);
        ApiResponse ans = new ApiResponse();
        String lowerPrompt = prompt.toLowerCase();
        String result;

        if (lowerPrompt.contains("price") || lowerPrompt.contains("value")) {
            result = "Current price of " + coinDetails.getName() + " is ₹" + coinDetails.getCurrentPrice();
        } else if (lowerPrompt.contains("market cap")) {
            result = "Market cap of " + coinDetails.getName() + " is ₹" + coinDetails.getMarketCap();
        } else if (lowerPrompt.contains("volume")) {
            result = "Total volume of " + coinDetails.getName() + " is ₹" + coinDetails.getTotalVolume();
        } else if (lowerPrompt.contains("rank")) {
            result = coinDetails.getName() + " is ranked #" + coinDetails.getMarketCapRank();
        } else if (lowerPrompt.contains("high")) {
            result = "24h high of " + coinDetails.getName() + " is ₹" + coinDetails.getHigh24h();
        } else if (lowerPrompt.contains("low")) {
            result = "24h low of " + coinDetails.getName() + " is ₹" + coinDetails.getLow24h();
        } else if (lowerPrompt.contains("supply")) {
            result = "Circulating supply of " + coinDetails.getName() + " is " + coinDetails.getCirculatingSupply();
        } else {
            result = "Sorry, I couldn’t understand your query. Here's full info:\n" + coinDetails.toString();
        }

        ans.setMessage(result);
        return ans;
    }

}
