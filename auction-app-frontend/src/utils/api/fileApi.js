import { API } from '../constants';

export const uploadImages = async (formData) => {
  try {
    const response = await API.post('/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    console.log(response.data);
  } catch (error) {
    console.error('Error uploading the files: ',error);
  }
};
