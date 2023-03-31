import React from 'react';
import './product.css';
import { Link } from 'react-router-dom';

const Product = ({ product: { id, productName, startPrice, images } }) => {
  return (
    <div className='product'>
      <img src={images[0]} alt='products' />
      <div className='product__details'>
        <Link to={`/product/${id}`}>{productName}</Link>
        <p>
          Start From &nbsp;<span>${startPrice}</span>
        </p>
      </div>
    </div>
  );
};

export default Product;
