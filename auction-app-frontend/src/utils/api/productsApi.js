import { API } from "../constants"

export const getProduct = (id) => {
    return API.get(`/products/${id}`);
}