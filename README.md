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

**Login Page**  


**Portfolio Dashboard**  
![Portfolio Dashboard](screenshots/portfolio.png)  

**Buy/Sell Crypto**  
![Buy/Sell](screenshots/buy_sell.png)  

**Transaction History**  
![Transaction History](screenshots/transaction_history.png)  

**Demo Video:** [Watch on LinkedIn](https://www.linkedin.com/posts/sanya-dureja-13960122a_i-am-thrilled-to-share-that-i-have-successfully-activity-7086478696378724352-QwEw?utm_source=share&utm_medium=member_desktop)  

---

## ⚙️ How to Run Locally

### Backend

```bash
cd backend
./mvnw spring-boot:run
