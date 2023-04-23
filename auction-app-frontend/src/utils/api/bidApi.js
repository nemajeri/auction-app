import { API } from '../constants';
import { getJwtFromCookie } from '../helperFunctions';

export const getBidsForUser = (userId) => {
  return API.get(`/bids/app-user?userId=${userId}`);
};

export const updateBid = (id, bidAmount) => {
  const jwtToken = getJwtFromCookie();
  if (!jwtToken) {
    return;
  }

  return API.post(
    '/bid/create-bid',
    {
      productId: id,
      amount: bidAmount,
    },
    {
      headers: {
        Authorization: `Bearer ${jwtToken}`,
      },
    }
  );
};
