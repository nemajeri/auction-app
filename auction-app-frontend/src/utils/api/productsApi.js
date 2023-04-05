import { API } from '../constants';

export const getProducts = () => {
  return API.get('/products');
};

export const getProduct = (id) => {
  return API.get(`/products/${id}`);
};

export const getAllProducts = (
  pageNumber,
  pageSize,
  categoryId = null,
  searchTerm = ''
) => {
  let url = `/products/items?pageNumber=${pageNumber}&pageSize=${pageSize}`;

  if (categoryId !== null) {
    url += `&categoryId=${categoryId}`;
  }

  if (searchTerm !== '') {
    url += `&searchTerm=${searchTerm}`;
  }

  return API.get(url);
};

