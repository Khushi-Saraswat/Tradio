package com.trading.demo.service;

import com.trading.demo.response.ApiResponse;

public interface ChatBotService {
    ApiResponse getCoinDetails(String prompt);

    // CoinDTO getCoinByName(String coinName);

    // String simpleChat(String prompt);
}
