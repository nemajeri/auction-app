import axios from 'axios';

export const API = axios.create({
  baseURL: process.env.REACT_APP_BASE_URL,
});

export const AuthAPI = axios.create({
  baseURL: process.env.REACT_APP_BASE_URL,
  withCredentials: true,
});

export const PAGE_SIZE = 9;

export const LANDING_PAGE_SIZE = 8;

export const ALL_CATEGORIES_ID = 10;

export const EMAIL_VALIDATOR = /\S+@\S+\.\S+/;

export const PASSWORD_LENGTH = 8;

export const PASSWORD_VALIDATOR = /(?=.*[!@#$%^&*()_+[\]{}|\\;:'",./<>?`~])(?=.*\d)/;

export const NAME_VALIDATOR = /^[a-zA-Z\s]+$/;

