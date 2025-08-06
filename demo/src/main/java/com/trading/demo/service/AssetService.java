package com.trading.demo.service;

import java.util.List;

import com.trading.demo.model.Asset;
import com.trading.demo.model.Coin;
import com.trading.demo.model.Users;

public interface AssetService {
    Asset createAsset(Users user, Coin coin, double quantity);

    Asset getAssetById(Long assetId);

    Asset getAssetByUserAndId(Long userId, Long assetId);

    List<Asset> getUsersAssets(Long userId);

    Asset updateAsset(Long assetId, double quantity) throws Exception;

    Asset findAssetByUserIdAndCoinId(Long userId, String coinId) throws Exception;

    void deleteAsset(Long assetId);

}
