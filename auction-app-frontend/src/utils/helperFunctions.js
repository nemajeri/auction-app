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

export const validateFields = (formState) => {
  const errors = {};

  if (!formState.email || !/\S+@\S+\.\S+/.test(formState.email)) {
    errors.email = 'Please enter a valid email address';
  }

  if (!formState.password || formState.password.length < 5) {
    errors.password = 'Password must be at least 5 characters';
  } else if (!/(?=.*[A-Z])(?=.*\d)/.test(formState.password)) {
    errors.password =
      'Password must contain at least one uppercase letter and one number';
  }

  return errors;
};
