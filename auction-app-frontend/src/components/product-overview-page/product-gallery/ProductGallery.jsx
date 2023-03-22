import React, { useState } from 'react';
import './productGallery.css';

const ProductGallery = ({ images }) => {
  const [mainImage, setMainImage] = useState(images[5] || '/images/shoe-15.jpg');

  const handleImageClick = (event) => {
    setMainImage(event.target.src);
  };

  return (
    <div className='gallery'>
      <img src={mainImage} alt='individual-product' />
      <div className='gallery__product--images'>
        <img
          src={images[0]}
          alt='individual-product'
          onClick={handleImageClick}
        />
        <img
          src={images[1]}
          alt='individual-product'
          onClick={handleImageClick}
        />
        <img
          src={images[2]}
          alt='individual-product'
          onClick={handleImageClick}
        />
        <img
          src={images[3]}
          alt='individual-product'
          onClick={handleImageClick}
        />
        <img
          src={images[4]}
          alt='individual-product'
          onClick={handleImageClick}
        />
      </div>
    </div>
  )
};

export default ProductGallery;
