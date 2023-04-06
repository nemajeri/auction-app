export const getTotalPages = (products, size) => {
    return Math.ceil(products?.totalElements / size);
  }