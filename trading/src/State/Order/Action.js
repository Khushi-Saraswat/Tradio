import api from "@/config/api";
import * as types from './ActionTypes';

// Action Creators
export const payOrder = ({ jwt, orderData, amount }) => async (dispatch) => {
  dispatch({ type: types.PAY_ORDER_REQUEST });

  console.log("🚀 Sending Order Data:", orderData);
  console.log("💰 Order Amount:", amount);

  try {
    const response = await api.post('/api/orders/pay', orderData, {
      headers: {
        Authorization: `Bearer ${jwt}`
      },
    });

    dispatch({
      type: types.PAY_ORDER_SUCCESS,
      payload: response.data,
      amount,
    });

    console.log("✅ Order Success:", response.data);
  } catch (error) {
    console.error("❌ Error while processing payment:", error);

    if (error.response) {
      // The request was made and the server responded with a status code outside 2xx
      console.error("🚨 Backend Error Response:", error.response.data);
      console.error("📦 Status Code:", error.response.status);
    } else if (error.request) {
      // The request was made but no response was received
      console.error("📡 No Response received from backend:", error.request);
    } else {
      // Something happened while setting up the request
      console.error("⚙️ Request Setup Error:", error.message);
    }

    dispatch({
      type: types.PAY_ORDER_FAILURE,
      error: error.response?.data?.message || error.message,
    });
  }
};

export const getOrderById = (jwt, orderId) => async (dispatch) => {
  dispatch({ type: types.GET_ORDER_REQUEST });

  try {
    const response = await api.get(`/api/orders/${orderId}`, {
      headers: {
        Authorization: `Bearer ${jwt}`
      },
    });

    dispatch({
      type: types.GET_ORDER_SUCCESS,
      payload: response.data,
    });
  } catch (error) {
    dispatch({
      type: types.GET_ORDER_FAILURE,
      error: error.message,
    });
  }
};

export const getAllOrdersForUser = ({jwt, orderType, assetSymbol}) => async (dispatch) => {
  dispatch({ type: types.GET_ALL_ORDERS_REQUEST });

  try {
    const response = await api.get('/api/orders/', {
      headers: {
        Authorization: `Bearer ${jwt}`
      },
      params: {
        order_type: orderType,
        asset_symbol: assetSymbol,
      },
    });

    dispatch({
      type: types.GET_ALL_ORDERS_SUCCESS,
      payload: response.data,
      
    });
    console.log("order success", response.data)
  } catch (error) {
    console.log("error ",error)
    dispatch({
      type: types.GET_ALL_ORDERS_FAILURE,
      error: error.message,
    });
  }
};
