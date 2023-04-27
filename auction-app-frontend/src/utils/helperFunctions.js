import {
  PASSWORD_VALIDATOR,
  EMAIL_VALIDATOR,
  PASSWORD_LENGTH,
  NAME_VALIDATOR,
  FORM_TYPES
} from './constants';

export const getTotalPages = (products, size) => {
  return Math.ceil(products?.totalElements / size);
};

export const calculateTimeLeft = (product) => {
  const startDate = new Date(product.startDate);
  const endDate = new Date(product.endDate);

  const differenceInDays = Math.round(
    (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)
  );

  const differenceInWeeks = Math.floor(differenceInDays / 7);

  if (differenceInWeeks > 0 && differenceInDays > 0) {
    return `${differenceInWeeks} weeks ${differenceInDays} days`;
  }
  return;
};

export const getJwtFromCookie = () => {
  const prefix = 'auction_app';
  const allCookies = document.cookie.split(';');

  for (let i = 0; i < allCookies.length; i++) {
    let cookie = allCookies[i].trim();

    if (cookie.startsWith(prefix)) {
      const cookieName = cookie.split('=')[0].trim();
      return cookie.substring((cookieName + '=').length, cookie.length);
    }
  }

  return null;
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
