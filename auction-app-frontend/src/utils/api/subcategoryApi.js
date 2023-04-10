import { API } from "../constants"

export const getSubcategories = (categoryId) => {
    return API.get(`/subcategories/category?categoryId=${categoryId}`);
  };