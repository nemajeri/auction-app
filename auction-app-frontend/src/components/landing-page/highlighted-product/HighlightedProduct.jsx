import React from 'react';
import './highlightedProduct.css';
import Button from '../../../utils/Button';

const HighlightedProduct = ({
  highlightedProduct,
}) => {
  const handleButtonClicked = () => {
    console.log('clicked');
  };

  return (
    <div className='highlighted-product'>
      <div className='highlighted-product__text'>
        <h2>{highlightedProduct.productName}</h2>
        <span>Start From {highlightedProduct.startPrice}$</span>
        <p>{highlightedProduct.description}</p>
        <Button children={'BID NOW'} onClick={handleButtonClicked} className={'highlighted-product__button'}/>
      </div>
      <img src={highlightedProduct.images[0]} alt='highlighted-product' />
    </div>
  );
};

export default HighlightedProduct;
