import { API } from '../constants';

export const getProducts = () => {
  return API.get('/products');
};

export const getProduct = (id) => {
  return API.get(`/products/${id}`);
};

export const getAllProdcutsByCategoryAndSearchTerm = (
  pageNumber,
  pageSize,
  categoryId,
  searchTerm = ''
) => {
  return API.get(
    `/products/items/category?pageNumber=${pageNumber}&pageSize=${pageSize}&categoryId=${categoryId}&searchTerm=${searchTerm}`
  );
};

