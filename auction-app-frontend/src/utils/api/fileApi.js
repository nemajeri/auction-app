import { API } from '../constants';

export const uploadImages = (imageData, productDetails) => {
  const formData = new FormData();
  Object.keys(productDetails).forEach((key) => {
    formData.append(key, productDetails[key]);
  });
  imageData.forEach((file) => {
    formData.append('files', file);
  });

  return API.post('/add-item-with-images', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};
