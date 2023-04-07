import axios from 'axios';

export const API = axios.create({
    baseURL: process.env.REACT_APP_PRODUCTS_URL,
  });

export const PAGE_SIZE = 9;

export const LANDING_PAGE_SIZE = 8;

