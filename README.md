# 💹 Tradio: Crypto Trading Platform

A full-stack cryptocurrency trading platform that allows users to buy/sell crypto, manage their portfolio, view transaction history, and securely handle funds — all in real-time.

---

## 🛠 Technologies Used

**Frontend:** React.js, Tailwind CSS, Redux, Axios, React-Router-Dom  
**Backend:** Java, Spring Boot, MySQL, Spring Security, JWT, OAuth2, Spring Scheduler  
**APIs:** CoinGecko (for live coin data), Razorpay (for simulated wallet top-up)  

---

## 📄 Project Description

- Developed a **full-stack cryptocurrency trading platform** using React.js for the frontend and Spring Boot for the backend.  
- Integrated **CoinGecko API** to fetch real-time cryptocurrency data.  
- Implemented **buy/sell simulation** for crypto trading using **mock data** without real money transactions.  
- Built **wallet functionality**: virtual balance top-up, wallet-to-wallet transfer, and withdrawal simulation.  
- Implemented **JWT-based authentication**, backend-only OAuth2 login with Google, and role-based access (Admin/User).  
- Added **Two-Factor Authentication (2FA)** and Forgot Password via Email OTP.  
- Used **Spring Scheduler** to simulate portfolio updates and market fluctuations based on **mock data**.  
- Designed a clean **dashboard for portfolio management, transaction history, and coin search**.  
- Implemented a **manual logic-based ChatBot** to answer crypto-related questions (price, market cap, volume, rank, supply).
- Integrated **Razorpay** to simulate wallet top-ups using a **test account**.  
- All payments are fake (sandbox mode) and for demonstration purposes only.  
- Admin and user balances update based on simulated top-ups and withdrawals.

  

---

## 📈 Features

- **Buy & Sell Crypto:** Simulate trading with real-time prices.  
- **Portfolio Management:** Track holdings, balance, profit/loss, coin-wise distribution.  
- **Wallet Functionality:** Top-up, transfer, withdraw simulated currency via **Razorpay test account**.
- **Transaction History:** Logs for all wallet activities and withdrawals.  
- **Search Coin:** Fetch live coin details from CoinGecko API.  
- **ChatBot:** Ask crypto-related questions (price, market cap, volume, rank, supply) using a manual logic-based chatbot.  
- **Authentication & Security:** JWT, backend OAuth2, 2FA, role-based access.
- **Admin Withdrawal Management:** Admin can review and approve/reject withdrawal requests from all users. Virtual wallet balances and transaction history are updated automatically.


---

## 🌐 Dataset / API

- **Coin data:** [CoinGecko API](https://www.coingecko.com/en/api) – free, no API key required.  
- **Payment simulation:** Razorpay API (for virtual top-ups).  

---

## 🎬 Walkthrough / Demo

## 🏠 Home Page  
![Home](https://raw.githubusercontent.com/Khushi-Saraswat/Tradio/main/trading/public/home.PNG)

## 🔐 Login Page  
![Login](https://raw.githubusercontent.com/Khushi-Saraswat/Tradio/main/trading/public/login.PNG)

## 💳 Payment Page  
![Payment](https://raw.githubusercontent.com/Khushi-Saraswat/Tradio/main/trading/public/payment.PNG)

## 📊 Portfolio  
![Portfolio](https://raw.githubusercontent.com/Khushi-Saraswat/Tradio/main/trading/public/portfolio.PNG)

## 👤 Profile  
![Profile](https://raw.githubusercontent.com/Khushi-Saraswat/Tradio/main/trading/public/profile.PNG)

## 👛 Wallet  
![Wallet](https://raw.githubusercontent.com/Khushi-Saraswat/Tradio/main/trading/public/wallet.PNG)

## 📈 Watchlist  
![Watchlist](https://raw.githubusercontent.com/Khushi-Saraswat/Tradio/main/trading/public/watchlist.PNG)

## 📑 Details  
![Details](https://raw.githubusercontent.com/Khushi-Saraswat/Tradio/main/trading/public/details.PNG)

## 🛠️ Admin Withdrawals  
![Admin Withdrawals](https://raw.githubusercontent.com/Khushi-Saraswat/Tradio/main/trading/public/adminwithdrawls.PNG)




---

## ⚙️ How to Run Locally  

### 🔹 Backend  

```bash
cd backend
./mvnw spring-boot:run

OR (Windows PowerShell)
cd backend
mvnw spring-boot:run



🔹 Frontend
cd trading
npm install
npm start

/

🗄️ Database Setup (PostgreSQL)

Install PostgreSQL & start service

Open terminal and login:

psql -U postgres


Create database:

CREATE DATABASE trading_platform;


Update application.properties (backend):

spring.datasource.url=jdbc:postgresql://localhost:5432/trading_platform
spring.datasource.username=postgres
spring.datasource.password=yourpassword


razorpay.key_id=your_test_key
razorpay.key_secret=your_test_secret






