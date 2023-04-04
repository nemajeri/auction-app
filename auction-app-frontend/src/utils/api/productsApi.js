import { API } from '../constants';

export const getProducts = () => {
  return API.get('/products');
};

export const getProduct = (id) => {
  return API.get(`/products/${id}`);
};

export const getAllProductsByCategory = (pageNumber, pageSize, categoryId) => {
  return API.get(
    `/products/items/category?pageNumber=${pageNumber}&pageSize=${pageSize}&categoryId=${categoryId}`
  );
};

export const getAllProductsBySearchTerm = (searchTerm = '') => {
  return API.get(`/products/items?searchTerm=${searchTerm}`);
};
