import React from 'react';
import './highlightedProduct.css';
import Button from '../../../utils/button/Button';

const HighlightedProduct = ({
  highlightedProduct: { name, price, description, image },
}) => {
  const handleButtonClicked = () => {
    console.log('clicked');
  };

  return (
    <div className='highlighted-product'>
      <div className='highlighted-product__text'>
        <h2>{name}</h2>
        <span>Start From {price}$</span>
        <p>{description}</p>
        <Button children={'BID NOW'} onClick={handleButtonClicked} />
      </div>
      <img src={image} alt='highlighted-product' />
    </div>
  );
};

export default HighlightedProduct;
