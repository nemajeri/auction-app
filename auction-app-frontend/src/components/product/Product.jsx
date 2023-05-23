import React, { useState } from 'react';
import './product.css';
import { Link } from 'react-router-dom';
import LoadingSpinner from '../loading-spinner/LoadingSpinner';

const Product = ({
  product: { id, productName, startPrice, images },
  location = null,
  landingPageProductClassName
}) => {
  const [isLoading, setIsLoading] = useState(true);
  const coverImage = Array.isArray(images) ? images[0] : images;

  return (
    <div className={landingPageProductClassName ? `product ${landingPageProductClassName}` : 'product'}>
      {isLoading && <LoadingSpinner pageSpinner={false}/>}
      <img src={coverImage} alt='products' onLoad={() => setIsLoading(false)}  className={isLoading ? 'product-image-loading' : 'product-image-loaded'} />
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