# 💹 Crypto Trading Platform

A full-stack cryptocurrency trading platform simulating buy/sell, portfolio management, wallet transfers, and transaction history with secure authentication.

---

## 🚀 Features

- Buy/Sell Crypto (simulation, CoinGecko API)
- Portfolio Management (track balance, profit/loss)
- Wallet Functionality (top-up, transfer, withdrawal simulation)
- Transaction History (wallet & withdrawals)
- Authentication & Security:
  - JWT-based login
  - Backend-only Google OAuth2
  - Two-Factor Authentication (2FA)
  - Role-based access (Admin/User)
- Forgot Password with Email OTP
- Mock trading + scheduler for portfolio updates

---

## 🧰 Tech Stack

**Backend:** Java, Spring Boot, MySQL, Spring Security, JWT, OAuth2, JavaMailSender  
**Frontend:** React.js, Tailwind CSS, Redux, Axios, React-Router-Dom  
**APIs:** CoinGecko (live coin data), Razorpay (simulated top-up)  

---

## 🛠 Architecture

Frontend (React.js + Redux)
↓
Backend REST API (Spring Boot + MySQL + JWT + OAuth2)
↑
CoinGecko API for live coin data

Razorpay API for wallet top-up simulation

