import React from 'react';
import Product from '../../../components/product/Product';

const ShopPageProducts = ({ products, currentLocation }) => {
  return (
    <>
      {products?.map((product, index) => (
        <Product product={product} key={index} location={currentLocation}/>
      ))}
    </>
  );
};

export default ShopPageProducts;
