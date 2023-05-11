import axios from 'axios';
import { sellerPath, bidsPath } from './paths';
import { AiOutlineDollarCircle, AiOutlineUnorderedList } from 'react-icons/ai';
import { getStartOfTodayUTC } from './helperFunctions';

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

export const PASSWORD_VALIDATOR =
  /(?=.*[!@#$%^&*()_+[\]{}|\\;:'",./<>?`~])(?=.*\d)/;

export const NAME_VALIDATOR = /^[a-zA-Z\s]+$/;

export const START_PRICE_VALIDATOR = /^\d+(\.\d{1,2})?$/;

export const ADDRESS_VALIDATOR = /^\d+ [A-Z][a-z]*( [A-Z][a-z]*)* Street$/;

export const ZIP_CODE_VALIDATOR = /^\d{7}$/;

export const PHONE_NUMBER_VALIDATOR = /^\+?\d{10,15}$/;

export const myAccountTabs = [
  {
    title: 'Seller',
    path: sellerPath,
    icon: AiOutlineUnorderedList,
  },
  {
    title: 'Bids',
    path: bidsPath,
    icon: AiOutlineDollarCircle,
  },
];

export const ACTIVE = "Active";

export const SOLD = "Sold";

export const START_OF_TODAY_UTC =  getStartOfTodayUTC();

export const AUCTION_ENDED = "Auction ended";

export const COOKIE_NAME ="auction_app_token";

export const PAYMENT_CURRENCY ="usd";

export const PAYMENT_TYPE = "card";

export const FIELD_NAME = 'sort';

export const FIELD_PLACEHOLDER = 'Sort Products';

export const OPTION_DEFAULT_SORTING = 'DEFAULT_SORTING';

export const OPTION_START_DATE = 'START_DATE';

export const OPTION_END_DATE = 'END_DATE';

export const OPTION_PRICE_LOW_TO_HIGH = 'PRICE_LOW_TO_HIGH';

export const OPTION_PRICE_HIGH_TO_LOW = 'PRICE_HIGH_TO_LOW';