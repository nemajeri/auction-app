import { API, LANDING_PAGE_SIZE } from '../constants';

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
  return API.get('/products/items/app-user', {
    params: {
      userId: userId,
      type: type,
    },
  });
};
