import { AuthAPI, COOKIE_NAME } from '../constants';
import { getJwtFromCookie } from '../helperFunctions';

export const getBidsForUser = () => {
  const jwtToken = getJwtFromCookie(COOKIE_NAME);
  if (!jwtToken) {
    return;
  }

  return AuthAPI.get('/bid/app-user');
};

export const updateBid = ( id, bidAmount) => {
  const jwtToken = getJwtFromCookie(COOKIE_NAME);
  if (!jwtToken) {
    return;
  }

  return AuthAPI.post('/bid/create-bid', {
    productId: id,
    amount: bidAmount,
  });
};

