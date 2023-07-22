import React from 'react';
import Product from '../../../components/product/Product';
import LoadingSpinner from '../../loading-spinner/LoadingSpinner';

const ShopPageProducts = ({ products, currentLocation, loading }) => {
  return (
    <>
      {loading ? (
        <LoadingSpinner pageSpinner />
      ) : (
        products?.map((product, index) => (
          <Product product={product} key={index} location={currentLocation} />
        ))
      )}
    </>
  );
};

export default ShopPageProducts;
