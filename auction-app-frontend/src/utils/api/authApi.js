import { AuthAPI } from '../constants';
import { getJwtFromCookie } from '../helperFunctions';

export const registerUser = async (
  credentials,
  navigate,
  onError,
  onSuccess
) => {
  try {
    const response = await AuthAPI.post('/auth/register', credentials);
    if (response.status === 201) {
      onSuccess();
      navigate('/login');
    }
  } catch (error) {
    console.error(error);
    onError();
  }
  return;
};

export const loginUser = async (
  credentials,
  onLoginSuccess,
  navigate,
  onError,
  onSuccess,
  onInvalidCredentials,
) => {
  try {
    const response = await AuthAPI.post('/auth/login', credentials);
    if (response.status === 200 && onLoginSuccess) {
      const jwtToken = getJwtFromCookie();
      onLoginSuccess(jwtToken);
      onSuccess();

      setTimeout(() => {
        navigate('/');
      }, 1000);
    }
  } catch (error) {
    if (error.response && error.response.status === 401) {
      onInvalidCredentials();
    } else {
      console.error(error);
      onError();
    }
  }
};

export const callOAuth2LoginSuccess = async (
  provider,
  token,
  handleLoginSuccess,
  navigate
) => {
  try {
    const response = await AuthAPI.post(
      `/auth/oauth2-login-success`,
      {
        provider,
        token,
      },
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );

    if (response.status === 200) {
      const cookie = response.data;
      navigate('/');
      if (handleLoginSuccess) {
        const jwtToken = getJwtFromCookie();
        handleLoginSuccess(jwtToken);
      }
    } else {
      console.error('OAuth2 login failed: ', response);
    }
  } catch (error) {
    console.error('Error during OAuth2 login: ', error);
  }
};
