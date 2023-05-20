import React from 'react';
import './highlightedProduct.css';
import Button from '../../../utils/Button';
import { Link } from 'react-router-dom';
import { shopPagePathToProduct } from '../../../utils/paths';
import Image from '../../my-account-page/Image';

const HighlightedProduct = ({ highlightedProduct }) => {
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
        <Image src={highlightedProduct?.images[0]} alt='highlighted-product' />
      </div>
    </div>
  );
};

export default HighlightedProduct;
