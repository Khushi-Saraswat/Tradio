package com.trading.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trading.demo.model.Coin;
import com.trading.demo.service.CoinService;

@RestController
@RequestMapping("/api/coins")
@CrossOrigin("*")
public class CoinController {

    private final CoinService coinService;

    @Autowired
    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping
    public List<Coin> getCoins(@RequestParam(defaultValue = "1") int page) throws Exception {
        return coinService.getCoinList(page); // Returns List<Coin>
    }

    @GetMapping("/{id}")
    public String getCoinDetails(@PathVariable String id) throws Exception {
        return coinService.getCoinDetails(id); // Returns raw JSON
    }

    @GetMapping("/search")
    public String searchCoin(@RequestParam String keyword) throws Exception {
        return coinService.searchCoin(keyword); // Returns raw JSON (filtered list)
    }

    @GetMapping("/{id}/chart")
    public String getMarketChart(@PathVariable String id,
            @RequestParam(defaultValue = "30") int days) throws Exception {
        return coinService.getMarketChart(id, days); // Raw JSON history data
    }

    @GetMapping("/top")
    public String getTop50Coins() throws Exception {
        return coinService.getTop50CoinsByMarketCapRank(); // Returns raw JSON
    }

    @GetMapping("/trending")
    public String getTrendingCoins() throws Exception {
        return coinService.getTreadingCoins(); // Returns raw JSON
    }
}
