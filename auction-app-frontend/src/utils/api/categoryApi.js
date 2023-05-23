import { API } from "../constants"

export const getCategories = (cancelToken) => {
    return API.get("/categories", { cancelToken: cancelToken});
}