# 💹 Tradio (Crypto Trading Platform)

A full-stack cryptocurrency trading platform that allows users to buy/sell crypto, manage their portfolio, view transaction history, and securely handle funds — all in real-time.

---

## 🚀 Features

### 🔁 Buy & Sell Crypto
- User-friendly UI for buying/selling a wide array of cryptocurrencies.
- Coin data fetched in real-time using **CoinGecko API**.
- Manual trade simulation (no real money involved).

### 📊 Portfolio Management
- Track current holdings and investment performance.
- View total balance, profit/loss, and coin-wise distribution.

### 💼 Wallet Functionality
- **Wallet to Wallet Transfer** – Send crypto to other users securely.
- **Add Balance** – Top-up wallet with virtual INR/USD.
- **Withdraw to Bank** – Simulated bank withdrawal functionality.

### 🧾 Transaction History
- **Wallet History** – Logs for all deposits, buys, sells, transfers.
- **Withdrawal History** – View and track past withdrawals.

### 🔍 Search Coin
- Search and view details of any cryptocurrency using live data from **CoinGecko API**.

### 🔐 Authentication & Security
- **Login / Register**
- **Forgot Password** with email OTP (JavaMailSender)
- **Two-Factor Authentication (2FA)** using OTP
- Role-based access control (Admin/User)

---

## 🧠 Architecture Overview

```text
Spring Boot (REST API) ←→ MySQL (Data) ←→ React.js (Frontend)
           ↑                        ↑
       CoinGecko API           Razorpay API (for virtual top-ups)


Here’s a professional and **placement/project-ready `README.md`** for your **Crypto Trading Platform**, including:

* Full project description
* Features
* Tech stack
* CoinGecko API usage
* Manual buy/sell implementation
* Folder structure
* Setup instructions

---

### ✅ `README.md` (You can copy this into your GitHub repo):

````markdown
# 💹 Crypto Trading Platform

A full-stack cryptocurrency trading platform that allows users to buy/sell crypto, manage their portfolio, view transaction history, and securely handle funds — all in real-time.

---

## 🚀 Features

### 🔁 Buy & Sell Crypto
- User-friendly UI for buying/selling a wide array of cryptocurrencies.
- Coin data fetched in real-time using **CoinGecko API**.
- Manual trade simulation (no real money involved).

### 📊 Portfolio Management
- Track current holdings and investment performance.
- View total balance, profit/loss, and coin-wise distribution.

### 💼 Wallet Functionality
- **Wallet to Wallet Transfer** – Send crypto to other users securely.
- **Add Balance** – Top-up wallet with virtual INR/USD.
- **Withdraw to Bank** – Simulated bank withdrawal functionality.

### 🧾 Transaction History
- **Wallet History** – Logs for all deposits, buys, sells, transfers.
- **Withdrawal History** – View and track past withdrawals.

### 🔍 Search Coin
- Search and view details of any cryptocurrency using live data from **CoinGecko API**.

### 🔐 Authentication & Security
- **Login / Register**
- **Forgot Password** with email OTP (JavaMailSender)
- **Two-Factor Authentication (2FA)** using OTP
- Role-based access control (Admin/User)

---

## 🧠 Architecture Overview

```text
Spring Boot (REST API) ←→ MySQL (Data) ←→ React.js (Frontend)
           ↑                        ↑
       CoinGecko API           Razorpay API (for virtual top-ups)
````

---

## 🧰 Tech Stack

### Backend 🧪

* Spring Boot
* MySQL
* Spring Security
* Java Mail Sender (OTP & Forgot Password)
* CoinGecko Public API
* Razorpay (for balance top-up)

### Frontend 🎨

* React.js
* Tailwind CSS
* Redux
* Axios
* Shadcn UI
* React-Router-Dom

---

## 🔗 CoinGecko API Integration

We use the free [CoinGecko API](https://www.coingecko.com/en/api) to fetch:

* Coin list: `/api/v3/coins/list`
* Current price: `/api/v3/simple/price`
* Coin details: `/api/v3/coins/{id}`

**Example Usage**:

```java
RestTemplate restTemplate = new RestTemplate();
String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=inr";
ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
```

✅ No API key needed, unlimited access for development.

---

## 🛠 Folder Structure

```text
/trading-platform
│
├── backend
│   ├── src/main/java/com/project/trading
│   │   ├── controller
│   │   ├── service
│   │   ├── model
│   │   ├── dto
│   │   └── repository
│   └── application.properties
│
├── trading-front
│   ├── src
│   │   ├── components
│   │   ├── pages
│   │   ├── redux
│   │   ├── routes
│   │   └── App.jsx
│   └── tailwind.config.js
```

---

## 🧪 Manual Buy/Sell Logic (Sample)

### Buy Crypto:

* User selects coin and INR amount.
* Backend fetches current price from CoinGecko.
* Calculate quantity = amount / current\_price.
* Update portfolio and wallet balance.

### Sell Crypto:

* User selects coin and quantity.
* Fetch price → total = quantity \* price.
* Add to wallet balance and update portfolio.

---

## 💳 Razorpay Integration

Used for simulated wallet top-up.

* Initiate order via Razorpay API
* On success, update virtual wallet balance

---

## 🧾 How to Run Locally

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

```bash
cd trading-front
npm install
npm run dev
```

---

## 📦 Future Enhancements

* Real-time WebSocket price updates
* Support real money trading (via exchange API)
* Full KYC user verification
* Leaderboard for top traders

---

## 🙋‍♂️ Made By

> Built with ❤️ by Khushi Saraswat
> Java Backend Developer | React | Spring Boot | MySQL


