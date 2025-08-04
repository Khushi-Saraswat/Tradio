import { applyMiddleware, combineReducers, legacy_createStore } from "redux";
import { thunk } from "redux-thunk";
import assetReducer from "./Assests/Reducer";
import authReducer from "./Auth/Reducer";
import chatBotReducer from "./Chat/Reducer";
import coinReducer from "./Coin/Reducer";
import orderReducer from "./Order/Reducer";
import walletReducer from "./Wallet/Reducer";
import watchlistReducer from "./WatchList/Reducer";
import withdrawalReducer from "./Withdrawal/Reducer";


const rootReducer = combineReducers(
    {
         auth:authReducer,
         coin:coinReducer,
         wallet:walletReducer,
         withdrawal:withdrawalReducer,
         order:orderReducer,
         asset: assetReducer,
         watchlist:watchlistReducer,
         chatBot:chatBotReducer

         

        

    }
);

export const store=legacy_createStore(rootReducer,applyMiddleware(thunk))