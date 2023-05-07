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

export const validateFormFields = (formData, fieldsArray) => {
  const errors = {};

  const validateField = (field, value) => {
    if (!field.validation(value)) {
      errors[field.name] = field.errorMessage;
    }
  };

  const flattenedFields = fieldsArray.flatMap((field) =>
    field.fields ? field.fields : field
  );

  flattenedFields.forEach((field) => {
    validateField(field, formData[field.name]);
  });

  return errors;
};

export const getTodayWithoutTime = () => {
  const today = new Date();
  return new Date(today.getFullYear(), today.getMonth(), today.getDate());
};