import { API } from "../constants"

export const getBidsForUser = (userId) => {
  return API.get(`/bids/app-user?userId=${userId}`);
};
