import { API_BASE_URL } from "@/config/api";
import axios from "axios";
import { ENABLE_TWO_STEP_AUTHENTICATION_FAILURE, ENABLE_TWO_STEP_AUTHENTICATION_REQUEST, ENABLE_TWO_STEP_AUTHENTICATION_SUCCESS, GET_USER_FAILURE, GET_USER_REQUEST, GET_USER_SUCCESS, LOGIN_FAILURE, LOGIN_REQUEST, LOGIN_SUCCESS, LOGIN_TWO_STEP_FAILURE, LOGIN_TWO_STEP_REQUEST, LOGIN_TWO_STEP_SUCCESS, LOGOUT, REGISTER_FAILURE, REGISTER_REQUEST, REGISTER_SUCCESS, SEND_RESET_PASSWORD_OTP_FAILURE, SEND_RESET_PASSWORD_OTP_REQUEST, SEND_RESET_PASSWORD_OTP_SUCCESS, SEND_VERIFICATION_OTP_FAILURE, SEND_VERIFICATION_OTP_REQUEST, SEND_VERIFICATION_OTP_SUCCESS, VERIFY_OTP_FAILURE, VERIFY_OTP_REQUEST, VERIFY_OTP_SUCCESS, VERIFY_RESET_PASSWORD_OTP_FAILURE, VERIFY_RESET_PASSWORD_OTP_REQUEST, VERIFY_RESET_PASSWORD_OTP_SUCCESS } from "./ActionTypes";


export const register=(userData) => async(dispatch) =>{
    dispatch({type:REGISTER_REQUEST})
    const baseurl="http://localhost:5455/"

    try{
     console.log(userData.data+"userData in action");
     const response=await axios.post(`${baseurl}auth/signup`,userData.data);
     const user=response.data;
     if(user.jwt){
     localStorage.setItem("jwt",user.jwt);
     }
     dispatch({type:REGISTER_SUCCESS,payload:user})
     userData.navigate("/")

    }catch(error){
     dispatch({type:REGISTER_FAILURE,payload:error.message})
     console.log(error.message);
    }
}


export const login=(userData) => async(dispatch) =>{


    dispatch({type:LOGIN_REQUEST})


    const baseurl="http://localhost:5455/"
    

    try{
     const response=await axios.post(`${baseurl}auth/signin`,userData.data);
     const user=response.data;
     console.log(user+"login user");
     
     if(user.twoFactorAuthEnabled)
     {
          userData.navigate(`/two-factor-auth/${user.session}`);
     }
      
      else if(user.jwt){
        localStorage.setItem("jwt",user.jwt);
        console.log('login',user);
         userData.navigate("/")
     }
     dispatch({type:LOGIN_SUCCESS,payload:user.jwt})
    
    }catch(error){
     dispatch({type:LOGIN_FAILURE,payload:error.message})
     console.log(error);
    }
}



export const getUser=(jwt) => async(dispatch) =>{


    dispatch({type:GET_USER_REQUEST})


    const baseurl="http://localhost:5455/"

    try{
    
        const response = await axios.get(`${baseurl}api/users/profile`, {
            headers: {
                Authorization: `Bearer ${jwt}`
            }
        });
     const user=response.data;
     console.log("user in action profile",user);
     console.log(user);
     dispatch({type:GET_USER_SUCCESS,payload:user})
    }catch(error){
     dispatch({type:GET_USER_FAILURE,payload:error.message})
     console.log(error);
    }
}


export const twoStepVerification = ({otp,session,navigate}) => async(dispatch) =>{

    
    dispatch({ type:LOGIN_TWO_STEP_REQUEST });
    try {
      const response = await axios.post(
        `${API_BASE_URL}/auth/two-factor/otp/${otp}`,
        {},
        {
          params: { id: session },
        }
      );
      const user = response.data;
      console.log(user+"2 step verification.....");

      if (user.jwt) {
        localStorage.setItem("jwt", user.jwt);
        console.log("login ", user);
        await dispatch(getUser(user.jwt));
        navigate("/");
      }
      dispatch({ type: LOGIN_TWO_STEP_SUCCESS, payload: user.jwt });
    } catch (error) {
      console.log("catch error", error);
      dispatch({
        type: LOGIN_TWO_STEP_FAILURE,
        payload: error.response?.data ? error.response.data : error,
      });
    }
};



export const sendVerificationOtp = ({ jwt, verificationType }) => {
  return async (dispatch) => {
    console.log(verificationType+"verficationType in sendVerificationOtp");
    dispatch({ type: SEND_VERIFICATION_OTP_REQUEST });
    const baseurl = "http://localhost:5455"; // Use your backend base URL or API_BASE_URL
    try {
      const response = await axios.post(
        `${baseurl}/api/users/verification/${verificationType}/send-otp`,
        {}, // No body needed
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      );
      console.log("response ", response);
      console.log("response data ", response.data);
      console.log("jwt ", jwt);
      console.log("verificationType ", verificationType);
      const user = response.data;
      dispatch({
        type: SEND_VERIFICATION_OTP_SUCCESS,
        payload: user,
      });
      console.log("send otp ", user);
    } catch (error) {
      console.log("error ", error);
      const errorMessage = error.message;
      dispatch({
        type: SEND_VERIFICATION_OTP_FAILURE,
        payload: errorMessage,
      });
    }
  };
};

export const verifyOtp = ({ jwt, otp }) => {
  console.log("jwt", jwt);
  return async (dispatch) => {
    dispatch({ type: VERIFY_OTP_REQUEST });
    try {
      const response = await axios.patch(
        `${API_BASE_URL}/api/users/verification/verify-otp/${otp}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      );
      const user = response.data;
      dispatch({ type: VERIFY_OTP_SUCCESS, payload: user });
      console.log("verify otp ", user);
    } catch (error) {
      console.log("error ", error);
      const errorMessage = error.message;
      dispatch({ type:VERIFY_OTP_FAILURE, payload: errorMessage });
    }
  };
};

export const enableTwoStepAuthentication = ({ jwt, otp }) => {
  console.log("jwt", jwt);
  console.log("otp", otp);
  return async (dispatch) => {
    dispatch({ type: ENABLE_TWO_STEP_AUTHENTICATION_REQUEST });
    try {
      const response = await axios.patch(
        `${API_BASE_URL}/api/users/enable-two-factor/verify-otp/${otp}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${jwt}`,
          },
        }
      );
      const user = response.data;
      console.log("2stepverifrontend",user);
      dispatch({
        type: ENABLE_TWO_STEP_AUTHENTICATION_SUCCESS,
        payload: user,
      });
      console.log("enable two step authentication ", user);
    } catch (error) {
      console.log("error ", error);
      const errorMessage = error.message;
      dispatch({
        type: ENABLE_TWO_STEP_AUTHENTICATION_FAILURE,
        payload: errorMessage,
      });
    }
  };
};


export const sendResetPassowrdOTP = ({
  sendTo,
  verificationType,
  navigate,
}) => {
  console.log("send otp ", sendTo);
  return async (dispatch) => {
    dispatch({ type: SEND_RESET_PASSWORD_OTP_REQUEST});
    try {
      const response = await axios.post(
        `http://localhost:5455/auth/users/reset-password/send-otp`,
        {
          sendTo,
          verificationType,
        }
      );
      const user = response.data;
      navigate(`/reset-password/${user.session}`);
      dispatch({
        type: SEND_RESET_PASSWORD_OTP_SUCCESS,
        payload: user,
      });
      console.log("otp sent successfully ", user);
    } catch (error) {
      console.log("error ", error);
      const errorMessage = error.message;
      dispatch({
        type: SEND_RESET_PASSWORD_OTP_FAILURE,
        payload: errorMessage,
      });
    }
  };
};

export const verifyResetPassowrdOTP = ({
  otp,
  password,
  session,
  navigate,
}) => {
  return async (dispatch) => {
    dispatch({ type: VERIFY_RESET_PASSWORD_OTP_REQUEST });
    try {
      const response = await axios.patch(
        `${API_BASE_URL}/auth/users/reset-password/verify-otp`,
        {
          otp,
          password,
        },
        {
          params: {
            id: session,
          },
        }
      );
      const user = response.data;
      dispatch({
        type: VERIFY_RESET_PASSWORD_OTP_SUCCESS,
        payload: user,
      });
      navigate("/password-update-successfully");
      console.log("VERIFY otp successfully ", user);
    } catch (error) {
      console.log("error ", error);
      const errorMessage = error.message;
      console.log("error message ", errorMessage);
      dispatch({
        type: VERIFY_RESET_PASSWORD_OTP_FAILURE,
        payload: errorMessage,
      });
    }
  };
};

export const logout=() => async(dispatch) =>{

    
    dispatch({type:LOGOUT});


}



