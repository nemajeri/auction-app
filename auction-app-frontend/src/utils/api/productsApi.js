import {
  API,
  AuthAPI,
  LANDING_PAGE_SIZE,
  COOKIE_NAME,
  SORT_OPTIONS,
  EMPTY_STRING,
} from '../constants';
import { getJwtFromCookie } from '../helperFunctions';
import { toast } from 'react-toastify';
import { sellerPath } from '../paths';

export const getProducts = () => {
  return API.get('/products');
};

export const getProduct = (id) => {
  return AuthAPI.get(`/products/${id}`);
};

export const getAllProducts = (
  pageNumber,
  pageSize,
  searchTerm = EMPTY_STRING,
  categoryId = null,
  sortOption = SORT_OPTIONS.DEFAULT_SORTING
) => {
  let url = `/products/items?pageNumber=${pageNumber}&pageSize=${pageSize}`;

  if (searchTerm !== '') {
    url += `&searchTerm=${encodeURIComponent(searchTerm)}`;
  }

  if (categoryId !== null) {
    url += `&categoryId=${categoryId}`;
  }

  if (sortOption !== null) {
    url += `&sort=${sortOption}`;
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
  return API.get('/products/highlighted-products');
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

export const addNewItemForAuction = async (
  productDetails,
  images,
  setLoading,
  navigate
) => {
  try {
    const formData = new FormData();

    const productDetailsBlob = new Blob([JSON.stringify(productDetails)], {
      type: 'application/json',
    });
    formData.append('productDetails', productDetailsBlob);

    images.forEach((image) => {
      formData.append(`images`, image, image.name);
    });

    const response = await AuthAPI.post('/products/add-item', formData, {
      headers: { 'Content-Type': undefined },
    });

    if (response.status === 201) {
      toast.success('Product created.');
    }
  } catch (error) {
    if (error.response.status === 406) {
      toast.success('Please use the right format for product images.');
    }
    toast.error('Error creating product.');
  } finally {
    setLoading(false);
    navigate(sellerPath);
  }
};

export const addCsvFileForProccessing = async (
  csvFile,
  setShowCsvModal,
  setLoading,
  setCsvFile
) => {
  const formData = new FormData();
  formData.append('file', csvFile[0][0]);

  try {
    setLoading(true);
    const response = await AuthAPI.post('/products/upload-csv-file', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    if (response.status === 201) {
      toast.success('Products created');
    }
  } catch (error) {
    debugger;
    if (error.response.status === 422 || error.response.status === 406) {
      toast.error(
        'Unable to process CSV file. Please ensure the file is in the correct format and try again.'
      );
    } else if (error.response.status === 404) {
      toast.error('Category or Subcategory not found.');
    } else {
      toast.error('Unexpected error occurred while creating product.');
    }
  } finally {
    setShowCsvModal(false);
    setLoading(false);
    setCsvFile(null);
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
