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
  const cookieName = 'jwt';
  const allCookies = document.cookie.split(';');

  console.log('All cookies: ', allCookies)

  for (let i = 0; i < allCookies.length; i++) {
    let cookie = allCookies[i].trim();

    console.log('Cookie in getJwtFromCookie function: ', cookie)
    if (cookie.startsWith(cookieName + '=')) {
      return cookie.substring((cookieName + '=').length, cookie.length);
    }
  }

  return null;
};


