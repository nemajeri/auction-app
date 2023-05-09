import { API, AuthAPI, LANDING_PAGE_SIZE, COOKIE_NAME } from '../constants';
import { getJwtFromCookie } from '../helperFunctions';


export const getProducts = () => {
  return API.get('/products');
};

export const getProduct = (id) => {
  return AuthAPI.get(`/products/${id}`);
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
  const jwtToken = getJwtFromCookie(COOKIE_NAME);
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

export const addNewItemForAuction = async (productDetails, images, setShowModal, setModalMessage) => {
  try {
    const formData = new FormData();

    const productDetailsBlob = new Blob([JSON.stringify(productDetails)], { type: 'application/json' });
    formData.append('productDetails', productDetailsBlob);

    images.forEach((image) => {
      formData.append(`images`, image, image.name);
    });

    const response = await AuthAPI.post('/products/add-item', formData, {
      headers: { 'Content-Type': undefined },
    });

    if (response.status === 201) {
      console.log('Product created:', response.data);
      setModalMessage('Product created.'); 
      setShowModal(true);
    } else {
      console.error('Error creating product:', response.data);
      setModalMessage('Error creating product.'); 
      setShowModal(true);
    }
  } catch (error) {
    setModalMessage('Error creating product.');
    setShowModal(true);
  }
};

export const getRecommendedProducts = async (userId) => {
  try {
    const response = await API.get(`/products/recommended?userId=${userId}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching recommended products:', error);
    return [];
  }
};
