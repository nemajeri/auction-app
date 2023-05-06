import {
  PASSWORD_VALIDATOR,
  EMAIL_VALIDATOR,
  PASSWORD_LENGTH,
  NAME_VALIDATOR,
  FORM_TYPES,
} from './constants';
import Cookies from "js-cookie";

export const getTotalPages = (products, size) => {
  return Math.ceil(products?.totalElements / size);
};

export const calculateTimeLeft = (product) => {
  const currentDate = new Date();
  const endDate = new Date(product.endDate);

  const differenceInDays = Math.round(
    (endDate.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24)
  );

  const differenceInWeeks = Math.floor(differenceInDays / 7);

  const remainingDays = differenceInDays % 7;

  if (differenceInWeeks >= 0 && differenceInDays >= 0) {
    return `${differenceInWeeks} weeks ${remainingDays} days`;
  }
  return 'Auction ended';
};

export const getJwtFromCookie = () => {
  const jwtCookie = Cookies.get('auction_app_token');
  return jwtCookie || null;
};

export const hoursDiff = (date) => {
  const currentDate = new Date();
  const endDate = new Date(date);
  const diffInMilliseconds = endDate - currentDate;
  const diffInHours = diffInMilliseconds / (1000 * 60 * 60);

  if (diffInHours < 0) {
    return 0;
  }

  return parseFloat(diffInHours).toFixed(2);
};

export const validateFields = (formState, formType) => {
  const errors = {};

  if (formType === FORM_TYPES.REGISTER) {
    if (!formState.firstName || !NAME_VALIDATOR.test(formState.firstName)) {
      errors.firstName = 'Please enter a valid first name';
    }

    if (!formState.lastName || !NAME_VALIDATOR.test(formState.lastName)) {
      errors.lastName = 'Please enter a valid last name';
    }
  }

  if (!formState.email || !EMAIL_VALIDATOR.test(formState.email)) {
    errors.email = 'Please enter a valid email address';
  }

  if (!formState.password || formState.password.length <= PASSWORD_LENGTH) {
    errors.password = `Password must be at least ${PASSWORD_LENGTH} characters`;
  } else if (!PASSWORD_VALIDATOR.test(formState.password)) {
    errors.password =
      'Password must contain at least one special sign and one number';
  }

  return errors;
};
