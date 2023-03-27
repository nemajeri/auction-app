import axios from 'axios';

export const API = axios.create({
    baseURL: process.env.REACT_APP_PRODUCTS_URL,
  });

export const PAGE_SIZE = 9;
export const PAGE_NUMBER = 0;