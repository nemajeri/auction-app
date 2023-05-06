import axios from 'axios';
import {
  sellerPath,
  bidsPath,
} from "./paths";
import { AiOutlineDollarCircle, AiOutlineUnorderedList } from 'react-icons/ai';

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

export const FORM_TYPES = {
  REGISTER: 'register',
  LOGIN: 'login',
};

export const myAccountTabs = [
  {
    title: "Seller",
    path: sellerPath,
    icon: AiOutlineUnorderedList,
  },
  {
    title: "Bids",
    path: bidsPath,
    icon: AiOutlineDollarCircle,
  },
];

export const ACTIVE = "Active";

export const SOLD = "Sold";
