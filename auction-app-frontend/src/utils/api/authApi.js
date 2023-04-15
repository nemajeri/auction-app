import { AuthAPI  } from "../constants"

export const registerUser = (formData) => AuthAPI.post("/auth/register", formData);
export const loginUser = (formData) => AuthAPI.post("/auth/login", formData);