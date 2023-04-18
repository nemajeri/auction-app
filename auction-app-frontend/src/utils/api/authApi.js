import { AuthAPI } from '../constants';
import { getJwtFromCookie } from '../helperFunctions';



export const registerUser = async (credentials, navigate) => {
  const response = await AuthAPI.post('/auth/register', credentials);
  if (response.status === 201) {
    navigate("/login");
  }
  return;
};

export const loginUser = async (credentials, onLoginSuccess, navigate) => {
  const response = await AuthAPI.post('/auth/login', credentials);
  if (response.status === 200 && onLoginSuccess) {
    const jwtToken = getJwtFromCookie();
    onLoginSuccess(jwtToken);
    navigate("/");
  }
  return response;
};

export const callOAuth2LoginSuccess = async (provider, token, navigate) => {
  try {
    const response = await AuthAPI.post(
      `/auth/oauth2-login-success?provider=${provider}&token=${token}`,
      {},
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );

    if (response.status === 200) {
      const cookie = response.data;
      navigate("/");
      console.log('OAuth2 login success: ', cookie);
    } else {
      console.error('OAuth2 login failed: ', response);
    }
  } catch (error) {
    console.error('Error during OAuth2 login: ', error);
  }
};
