import { API } from '../constants';

export const getProducts = () => {
  return API.get('/products');
};

export const getProductAsItems = (pageNumber, pageSize) => {
  return API.get(`/products/items?pageNumber=${pageNumber}&pageSize=${pageSize}`)
}

export const getProduct = (id) => {
  return API.get(`/products/${id}`);
};

export const getAllProductsByCategory = (
  pageNumber,
  pageSize,
  categoryId,
  searchTerm = ''
) => {
  return API.get(
    `/products/items/category?pageNumber=${pageNumber}&pageSize=${pageSize}&categoryId=${categoryId}&searchTerm=${searchTerm}`
  );
};
