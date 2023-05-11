import { AuthAPI, COOKIE_NAME } from '../constants';
import { getJwtFromCookie } from '../helperFunctions';

export const getUserByEmail = async (email) => {
  const jwtToken = getJwtFromCookie(COOKIE_NAME);
  if (!jwtToken) {
    return;
  }
  const response = await AuthAPI.get('/app-user/by-email', {
    params: {
      email: email,
    },
  });
  return response.data;
};

export const makePayment = (paymentMethodId, amount, currency, id) => {
  const jwtToken = getJwtFromCookie(COOKIE_NAME);
  if (!jwtToken) {
    return;
  }

  return AuthAPI.post('/payments', {
    paymentMethodId: paymentMethodId,
    amount: amount,
    currency: currency,
    productId: id
  });
};
