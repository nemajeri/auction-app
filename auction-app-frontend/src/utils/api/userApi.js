import { API } from "../constants"

export const getUserByEmail = async (email) => {
    const response = await API.get('/app-user/by-email', {
      params: {
        email: email,
      },
    });
    return response.data;
  };

