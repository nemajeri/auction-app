import { AuthAPI } from '../constants';
import { getJwtFromCookie } from '../helperFunctions';

export const registerUser = async (credentials, onRegisterSuccess) => {
  const response = await AuthAPI.post('/auth/register', credentials);
  if (response.status === 201 && onRegisterSuccess) {
    const jwtToken = getJwtFromCookie();
    onRegisterSuccess(jwtToken);
  }
  return response;
};

export const loginUser = async (credentials, onLoginSuccess) => {
  const response = await AuthAPI.post('/auth/login', credentials);
  if (response.status === 200 && onLoginSuccess) {
    const jwtToken = getJwtFromCookie();
    console.log('JWT token: ',jwtToken)
    onLoginSuccess(jwtToken);
  }
  return response;
};

export const callOAuth2LoginSuccess = async (provider, token) => {
  try {
    const response = await AuthAPI.post(
      '/auth/oauth2-login-success',
      {},
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `${provider} ${token}`,
        },
      }
    );

    if (response.status === 200) {
      const cookie = response.data;
      console.log('OAuth2 login success: ', cookie);
    } else {
      console.error('OAuth2 login failed: ', response);
    }
  } catch (error) {
    console.error('Error during OAuth2 login: ', error);
  }
};
