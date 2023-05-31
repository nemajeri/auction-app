import { API } from "../constants"

export const getNotifications = (userId) => {
    return API.get(`/get-notifications?userId=${userId}`);
}