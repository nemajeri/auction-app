import React from 'react';
import './product.css';

const Product = ({ product: { productName, startPrice, images } }) => {
  return (
    <div className='product'>
      <img src={images} alt='products' />
      <div className='product__details'>
        <h3>{productName}</h3>
        <p>
          Start From &nbsp;<span>${startPrice}</span>
        </p>
      </div>
    </div>
  );
};

export default Product;
