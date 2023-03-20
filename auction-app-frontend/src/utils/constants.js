import axios from 'axios';

export const API = axios.create({
  baseURL: process.env.REACT_APP_PRODUCTS_URL,
});

export const HIGHLIGHTED_PRODUCT = {
  name: 'Running Shoes',
  price: 59.99,
  description:
    'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Metus vulputate eu scelerisque felis imperdiet proin fermentum leo vel.',
  image: 'images/shoe-3.jpg',
};
