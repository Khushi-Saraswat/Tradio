import { Route, Routes, useLocation } from 'react-router-dom'
import './App.css'
import Activity from './page/Activity/Activity'
import Home from './page/Home/Home'
import PaymentDetails from './page/PaymentDetails/PaymentDetails'
import Portfolio from './page/Portfolio'
import Profile from './page/Profile/Profile'
import SearchCoin from './page/SearchCoin/SearchCoin'
import StockDetails from './page/Stock Details/StockDetails'
import Wallet from './page/Wallet/Wallet'
import Watchlist from './page/WatchList/Watchlist'
import Withdrawal from './Withdrawal/Withdrawal'

import { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import WithdrawalAdmin from './page/Admin/WithdrawalAdmin'
import Authe from './page/Auth/Authe'
import PasswordUpdateSuccess from './page/Auth/PasswordUpdateSuccess'
import ResetPasswordForm from './page/Auth/ResetPassoword'
import TwoFactorAuth from './page/Auth/TwoFactorAuth'
import Navbar from './page/Home/Navbar/Navbar'
import Notfound from './page/NotFound/NotFound'
import { getUser } from './State/Auth/Action'
import { shouldShowNavbar } from './utils/shouldShowNavbar'


const routes = [
  { path: "/", role: "ROLE_USER" },
  { path: "/portfolio", role: "ROLE_USER" },
  { path: "/activity", role: "ROLE_USER" },
  { path: "/wallet", role: "ROLE_USER" },
  { path: "/withdrawal", role: "ROLE_USER" },
  { path: "/payment-details", role: "ROLE_USER" },
  { path: "/wallet/success", role: "ROLE_USER" },
  { path: "/market/:id", role: "ROLE_USER" },
  { path: "/watchlist", role: "ROLE_USER" },
  { path: "/profile", role: "ROLE_USER" },
  { path: "/search", role: "ROLE_USER" },
  { path: "/admin/withdrawal", role: "ROLE_ADMIN" }
];

function App() {
    const location=useLocation();
    console.log(location+"location");
    console.log(location.pathname+"location.pathname");
    console.log(location+"location");
    const {auth}=useSelector(store=>store);
    const dispatch=useDispatch();
    console.log("auth ---- ",auth)
    console.log("authuser",auth.user);
   
     useEffect(()=>{

        dispatch(getUser(auth.jwt || localStorage.getItem("jwt")))
  
  
  },[auth.jwt])


   console.log("authuser",auth.user);
   console.log("location.pathname"+location.pathname);
   console.log("routes"+routes);

    const showNavbar=!auth.user?false:shouldShowNavbar(location.pathname,routes,auth.user?.role)
    console.log(showNavbar+"shownavbar");

 return (
    <>
    

      
      
      {auth.user ? (
        <>
         {showNavbar && <Navbar />}
          <Routes>
            <Route element={<Home />} path="/" />
            
            <Route element={<Portfolio />} path="/portfolio" />
            <Route element={<Activity />} path="/activity" />
            <Route element={<Wallet />} path="/wallet" />
            <Route element={<Withdrawal />} path="/withdrawal" />
            <Route element={<PaymentDetails />} path="/payment-details" />
            <Route element={<Wallet />} path="/wallet/:order_id" />
            <Route element={<StockDetails />} path="/market/:id" />
            <Route element={<Watchlist />} path="/watchlist" />
            <Route element={<Profile />} path="/profile" />
            <Route element={<SearchCoin />} path="/search" />
            {auth.user.role=="ROLE_ADMIN"&&<Route element={<WithdrawalAdmin/>} path="/admin/withdrawal" />}
            <Route element={<Notfound />} path="*" />
            
          </Routes>
        </>
      ) : (
        <>
          <Routes>
            <Route element={<Authe />} path="/" />
            <Route element={<Authe />} path="/signup" />
            <Route element={<Authe />} path="/signin" />
            <Route element={<Authe />} path="/forgot-password" />
           
            <Route element={<ResetPasswordForm />} path="/reset-password/:session" />
            <Route element={<PasswordUpdateSuccess />} path="/password-update-successfully" />
            <Route element={<TwoFactorAuth />} path="/two-factor-auth/:session" />
            <Route element={<Notfound />} path="*" />
          </Routes>
        </>
      )}
    </>



    

   
   
 );
}

export default App
