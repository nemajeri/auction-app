import { AuthAPI } from '../constants';
import { getJwtFromCookie } from '../helperFunctions';

export const getBidsForUser = (userId) => {
  const jwtToken = getJwtFromCookie('auction_app_token');
  if (!jwtToken) {
    return;
  }

  return AuthAPI.get(`/bid/app-user?userId=${userId}`);
};

export const updateBid = (id, bidAmount) => {
  const jwtToken = getJwtFromCookie('auction_app_token');
  if (!jwtToken) {
    return;
  }

  return AuthAPI.post('/bid/create-bid', {
    productId: id,
    amount: bidAmount,
  });
};

export const getHighestBidForUserAndProduct = (userId, productId) => {
  const jwtToken = getJwtFromCookie('auction_app_token');
  if (!jwtToken) {
    return;
  }

  return AuthAPI.get(`/bid/app-user/highest-bidder?userId=${userId}&productId=${productId}`);
};

