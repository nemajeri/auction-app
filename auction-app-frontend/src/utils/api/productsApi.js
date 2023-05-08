import { API, AuthAPI, LANDING_PAGE_SIZE } from '../constants';
import { getJwtFromCookie } from '../helperFunctions';

export const getProducts = () => {
  return API.get('/products');
};

export const getProduct = (id, token) => {
  return API.get(`/products/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};

export const getAllProducts = (
  pageNumber,
  pageSize,
  searchTerm = '',
  categoryId = null
) => {
  let url = `/products/items?pageNumber=${pageNumber}&pageSize=${pageSize}`;

  if (searchTerm !== '') {
    url += `&searchTerm=${searchTerm}`;
  }

  if (categoryId !== null) {
    url += `&categoryId=${categoryId}`;
  }

  return API.get(url);
};

export const getSortedProductsAccordingToDate = (
  filter,
  pageNumber = 0,
  size = LANDING_PAGE_SIZE
) => {
  return API.get(
    `/products/filtered-products?filter=${filter}&pageNumber=${pageNumber}&size=${size}`
  );
};

export const getAllProductsToSeparateHighlighted = () => {
  return API.get('/products/all-products');
};

export const getSearchSuggestion = (query) => {
  return API.get(`/products/search-suggestions?query=${query}`);
};

export const getProductsForUser = (userId, type) => {
  const jwtToken = getJwtFromCookie('auction_app_token');
  if (!jwtToken) {
    return;
  }

  return AuthAPI.get('/products/items/app-user', {
    params: {
      userId: userId,
      type: type,
    },
  });
};
