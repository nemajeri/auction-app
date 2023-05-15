import React from 'react';
import './product.css';
import { Link } from 'react-router-dom';

const Product = ({
  product: { id, productName, startPrice, images },
  location = null,
}) => {
  const coverImage = Array.isArray(images) ? images[0] : images;
  return (
    <div className='product'>
      <img src={coverImage} alt='products' />
      <div className='product__details'>
        {location ? (
          <Link to={`/${location}/product/${id}`}>{productName}</Link>
        ) : (
          <Link to={`/product/${id}`}>{productName}</Link>
        )}
        <p>
          Start From &nbsp;<span>${startPrice.toFixed(2)}</span>
        </p>
      </div>
    </div>
  );
};

export default Product;
