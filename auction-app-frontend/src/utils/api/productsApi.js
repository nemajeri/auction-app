import { API } from "../constants"

  export const getProducts = () => {
    return API.get("/products");
}

export const getProduct = (id) => {
    return API.get(`/products/${id}`);
}