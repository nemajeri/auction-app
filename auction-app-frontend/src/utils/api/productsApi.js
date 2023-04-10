import { API, LANDING_PAGE_SIZE } from '../constants';

export const getProducts = () => {
  return API.get('/products');
};

export const getProduct = (id) => {
  return API.get(`/products/${id}`);
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
