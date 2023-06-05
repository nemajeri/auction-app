import React, { useState } from 'react';
import './highlightedProduct.css';
import Button from '../../../utils/Button';
import { Link } from 'react-router-dom';
import { shopPagePathToProduct } from '../../../utils/paths';
import LoadingSpinner from '../../loading-spinner/LoadingSpinner';

const HighlightedProduct = ({ highlightedProduct }) => {
  const [isLoading, setIsLoading] = useState(true);
  return (
    <div className='highlighted-product'>
      <div className='highlighted-product__text'>
        <h2>{highlightedProduct?.productName}</h2>
        <span>Start From {highlightedProduct?.startPrice}$</span>
        <p>{highlightedProduct?.description}</p>
        <Link to={shopPagePathToProduct.replace(':id', highlightedProduct?.id)}>
          <Button
            children={'BID NOW'}
            className={'highlighted-product__button'}
          />
        </Link>
      </div>
      <div className='highlighted-product__image'>
      {isLoading && <LoadingSpinner pageSpinner={false}/>}
        <img
          src={highlightedProduct?.images}
          alt='highlighted-product'
          className={isLoading ? 'product-image-loading' : 'product-image-loaded'}
          onLoad={() => setIsLoading(false)}
        />
      </div>
    </div>
  );
};

export default HighlightedProduct;
