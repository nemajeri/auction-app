import { API } from "../constants"

export const getSortedNewAndLastProducts = (filter, page) => {
    return API.get(`/products/sorted-products?filter=${filter}&page=${page}`);
}

export const getProducts = () => {
    return API.get("/products");
}

export const getProduct = (id) => {
    return API.get(`/products/${id}`);
}