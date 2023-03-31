import { API } from "../constants"

export const getSortedNewAndLastProducts = (filter, pageNumber) => {
    return API.get(`/products/sorted-&-paginated-products?filter=${filter}&pageNumber=${pageNumber}`);
}

export const getAllProducts = () => {
    return API.get("/products/sorted-&-paginated-products?filter=all");
}


export const getProducts = () => {
    return API.get("/products");
}

export const getProduct = (id) => {
    return API.get(`/products/${id}`);
}