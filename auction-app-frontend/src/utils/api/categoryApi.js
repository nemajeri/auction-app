import { API } from "../constants"

export const getCategories = () => {
    return API.get("/categories");
}