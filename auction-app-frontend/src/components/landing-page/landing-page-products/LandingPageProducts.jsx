import React from 'react';
import Product from '../product/Product';

const LandingPageProducts = ({ products }) => {
  return (
    <>
      {products.map((product) => (
        <Product product={product} key={product.id} />
      ))}
    </>
  );
};

export default LandingPageProducts;
