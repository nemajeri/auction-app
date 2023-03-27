import React from 'react';
import Product from '../product/Product';

const ShopPageProducts = ({ products }) => {
  return (
    <>
      {products.map((product, index) => (
        <Product product={product} key={index} />
      ))}
    </>
  );
};

export default ShopPageProducts;
