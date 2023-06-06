import Cookies from 'js-cookie';
import { AUCTION_ENDED, COOKIE_NAME } from './constants';
import moment from 'moment/moment';
import * as Paths from '../utils/paths';

export const getTotalPages = (products, size) => {
  return Math.ceil(products?.totalElements / size);
};

export const calculateTimeLeft = (product) => {
  const currentDate = moment.utc();
  const endDate = moment.utc(product.endDate);

  const differenceInDays = endDate.diff(currentDate, 'days');
  const differenceInWeeks = Math.floor(differenceInDays / 7);

  const remainingDays = differenceInDays % 7;

  if (differenceInWeeks >= 0 && differenceInDays >= 0) {
    return `${differenceInWeeks} weeks ${remainingDays} days`;
  }
  return AUCTION_ENDED;
};

export const getJwtFromCookie = () => {
  const jwtCookie = Cookies.get(COOKIE_NAME);
  return jwtCookie || null;
};

export const hoursDiff = (date) => {
  const currentDate = moment.utc();
  const endDate = moment.utc(date);
  const diffInHours = endDate.diff(currentDate, 'hours', true); 

  if (diffInHours < 0) {
    return 0;
  }

  return parseFloat(diffInHours).toFixed(2);
};

export const validateFormFields = (formData, fieldsArray) => {
  const errors = {};

  const validateField = (field, value) => {
    if (field.validation && !field.validation(value)) {
      errors[field.name] = field.errorMessage;
    }
  };
  flattenFields(fieldsArray).forEach(field => validateField(field, formData[field.name]))

  return errors;
};

export const getStartOfTodayUTC = () => {
  return moment.utc().startOf('day');
};

export const flattenFields = (fields) => (
  fields.flatMap(field => field.fields ? field.fields : field)
);

export const isValidRoute = (pathname) => {
  const validRoutes = [
    '^' + Paths.shopPagePathToCategory.replace(':categoryId', '[A-Za-z0-9]+') + '$',
    '^' + Paths.shopPagePathToProduct.replace(':id', '[A-Za-z0-9]+') + '$',
    '^' + Paths.shopPagePath + '$',
    '^' + Paths.landingPagePath + '$',
    '^' + Paths.productOverviewPagePath.replace(':id', '[A-Za-z0-9]+') + '$',
    '^' + Paths.aboutUsPath + '$',
    '^' + Paths.privacyPolicyPath + '$',
    '^' + Paths.termsAndCondPath + '$',
    '^' + Paths.loginPath + '$',
    '^' + Paths.registrationPath + '$',
    '^' + Paths.myAccountPath + '(?:/[^/]+)?$',
    '^' + Paths.bidsPath + '$',
    '^' + Paths.sellerPath + '$',
    '^' + Paths.sellerToAddItemPath + '$'
  ];

  return validRoutes.some((route) => {
    const regex = new RegExp(route);
    return regex.test(pathname);
  });
};

export const formatDate = (dateString) => {
  return moment(dateString).format('YYYY-MM-DD HH:mm:ss');
};