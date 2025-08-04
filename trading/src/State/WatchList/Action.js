import api from "@/config/api";
import * as types from './ActionTypes';

// Action Creators
export const getUserWatchlist = ({jwt}) => async (dispatch) => {
  dispatch({ type: types.GET_USER_WATCHLIST_REQUEST });

  try {
    const response = await api.get('/api/watchlist/user',
            {
              
        headers: {
          Authorization: `Bearer ${jwt}`
        },
      }
    );

    console.log("getUserWatchlist response", response.data);

    dispatch({
      type: types.GET_USER_WATCHLIST_SUCCESS,
      payload: response.data,
    });
    console.log("User watchlist fetched successfully:", response.data);
  } catch (error) {
    dispatch({
      type: types.GET_USER_WATCHLIST_FAILURE,
      error: error.message,
    });
  }
};





export const addItemToWatchlist = ({ coinId, jwt }) => async (dispatch) => {
  dispatch({ type: types.ADD_COIN_TO_WATCHLIST_REQUEST });

  try {
    console.log("coinId", coinId);
    console.log("jwt", jwt);

    const response = await api.patch(
      `/api/watchlist/add/coin/${coinId}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${jwt}`
        },
      }
    );

    console.log("addToWatchlist response", response.data);

    dispatch({
      type: types.ADD_COIN_TO_WATCHLIST_SUCCESS,
      payload: response.data,
    });

    console.log("Coin added to watchlist successfully:", response.data);
  } catch (error) {
    console.log("error", error.message);
    dispatch({
      type: types.ADD_COIN_TO_WATCHLIST_FAILURE,
      error: error.message,
    });
  }
};
