import foolinks from '../data/foolinks.json';
import navlinks from '../data/navlinks.json';

export const shopPagePathToProduct = '/shop/product/:id';
export const shopPagePath = navlinks[1].route;
export const landingPagePath = navlinks[0].route;
export const productOverviewPagePath = '/product/:id';
export const aboutUsPath = foolinks[0].links[0].url;
export const privacyPolicyPath = foolinks[0].links[1].url;
export const termsAndCondPath = foolinks[0].links[2].url;
export const loginPath = "/login";
export const registrationPath = "/register";
export const myAccountPath = navlinks[2].route;
export const bidsPath = `${myAccountPath}/bids`;
export const sellerPath = `${myAccountPath}/seller`;