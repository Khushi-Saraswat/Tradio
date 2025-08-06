// src/api.js or similar
/*import axios from 'axios';

export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json"
  }
});

export default api;*/

import axios from 'axios';

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
console.log("Base URL:", API_BASE_URL);


const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json"
  }
});

export default api;

