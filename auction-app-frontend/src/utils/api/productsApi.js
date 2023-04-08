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

export const getSortedProductsAccordingToDate = (
    filter,
    pageNumber = 0,
    size = 8
  ) => {
    return API.get(
      `/products/filtered-products?filter=${filter}&pageNumber=${pageNumber}&size=${size}`
    );
  };
  

export const getAllProductsToSeparateHighlighted = () => {
    return API.get("/products/all-products");
}

