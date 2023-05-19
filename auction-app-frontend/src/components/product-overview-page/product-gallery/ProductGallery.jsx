import React, { useState } from 'react';
import './productGallery.css';
import NoImagePlaceholder from '../../../utils/NoImagePlaceholder';

const ProductGallery = ({ images }) => {
  const [mainImage, setMainImage] = useState(images[0]);
  const expectedImageCount = 5;

  return (
    <div className='gallery'>
      <img src={mainImage} alt='individual-product' />
      <div className='gallery__product--images'>
        {[...Array(expectedImageCount)].map((_, index) => {
          return (
            <div key={index} className='gallery__image-container'>
              {images[index] ? (
                <img
                  src={images[index]}
                  alt='individual-product'
                  onClick={(e) => setMainImage(e.target.src)}
                />
              ) : (
                <div className='no-image__placeholder-container'>
                  <NoImagePlaceholder />
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ProductGallery;
