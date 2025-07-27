package com.trading.demo.service;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.trading.demo.model.CoinDTO;
import com.trading.demo.response.ApiResponse;
import com.trading.demo.response.FunctionResponse;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${gemini.api.key}")
    private String API_KEY;

    @Value("${gemini.api.url}")
    private String url;

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

    public CoinDTO makeApiRequest(String currencyName) {
        System.out.println("coin name " + currencyName);
        String urls = url + currencyName.toLowerCase();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(urls, Map.class);
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
            coinInfo.setPriceChangePercentage24h(convertToDouble(marketData.get("price_change_percentage_24h")));
            coinInfo.setMarketCapChange24h(convertToDouble(marketData.get("market_cap_change_24h")));
            coinInfo.setMarketCapChangePercentage24h(
                    convertToDouble(marketData.get("market_cap_change_percentage_24h")));
            coinInfo.setCirculatingSupply(convertToDouble(marketData.get("circulating_supply")));
            coinInfo.setTotalSupply(convertToDouble(marketData.get("total_supply")));

            return coinInfo;
        }
        return null;
    }

    public FunctionResponse getFunctionResponse(String prompt) {
        String GEMINI_API_URL = url + "?key=" + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"parts\": [\n" +
                "        {\n" +
                "          \"text\": \"" + prompt + "\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"tools\": [\n" +
                "    {\n" +
                "      \"functionDeclarations\": [\n" +
                "        {\n" +
                "          \"name\": \"getCoinDetails\",\n" +
                "          \"description\": \"Get the coin details from given currency object\",\n" +
                "          \"parameters\": {\n" +
                "            \"type\": \"OBJECT\",\n" +
                "            \"properties\": {\n" +
                "              \"currencyName\": {\n" +
                "                \"type\": \"STRING\",\n" +
                "                \"description\": \"The currency name, id, symbol.\"\n" +
                "              },\n" +
                "              \"currencyData\": {\n" +
                "                \"type\": \"STRING\",\n" +
                "                \"description\": \"Currency Data like id, symbol, name, image, current_price, market_cap, etc.\"\n"
                +
                "              }\n" +
                "            },\n" +
                "            \"required\": [\"currencyName\", \"currencyData\"]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

        String responseBody = response.getBody();
        ReadContext ctx = JsonPath.parse(responseBody);

        FunctionResponse res = new FunctionResponse();
        res.setCurrencyName(ctx.read("$.candidates[0].content.parts[0].functionCall.args.currencyName"));
        res.setCurrencyData(ctx.read("$.candidates[0].content.parts[0].functionCall.args.currencyData"));
        res.setFunctionName(ctx.read("$.candidates[0].content.parts[0].functionCall.name"));

        return res;
    }

    @Override
    public ApiResponse getCoinDetails(String prompt) {
        FunctionResponse res = getFunctionResponse(prompt);
        String apiResponse = makeApiRequest(res.getCurrencyName()).toString();
        String GEMINI_API_URL = url + "?key=" + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{\n" +
                "  \"contents\": [\n" +
                "    {\"role\": \"user\", \"parts\": [{\"text\": \"" + prompt + "\"}]},\n" +
                "    {\"role\": \"model\", \"parts\": [{\"functionCall\": {\"name\": \"getCoinDetails\", \"args\": {\"currencyName\": \""
                + res.getCurrencyName() + "\", \"currencyData\": \"" + res.getCurrencyData() + "\"}}}]},\n" +
                "    {\"role\": \"function\", \"parts\": [{\"functionResponse\": {\"name\": \"getCoinDetails\", \"response\": {\"name\": \"getCoinDetails\", \"content\": "
                + apiResponse + "}}}]}\n" +
                "  ]\n" +
                "}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, request, String.class);

        ReadContext ctx = JsonPath.parse(response.getBody());
        String text = ctx.read("$.candidates[0].content.parts[0].text");
        ApiResponse ans = new ApiResponse();
        ans.setMessage(text);
        return ans;
    }

    @Override
    public CoinDTO getCoinByName(String coinName) {
        return this.makeApiRequest(coinName);
    }

    @Override
    public String simpleChat(String prompt) {
        String GEMINI_API_URL = url + "?key=" + API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        JSONArray contentsArray = new JSONArray();
        JSONObject contentsObject = new JSONObject();
        JSONArray partsArray = new JSONArray();
        JSONObject textObject = new JSONObject();
        textObject.put("text", prompt);
        partsArray.put(textObject);
        contentsObject.put("parts", partsArray);
        contentsArray.put(contentsObject);
        requestBody.put("contents", contentsArray);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);

        return response.getBody();
    }
}
