import React, { useState } from 'react';
import './productGallery.css';
import LoadingSpinner from '../../loading-spinner/LoadingSpinner';

const ProductGallery = ({ images }) => {
  const [mainImage, setMainImage] = useState(images[0]);
  const [isLoading, setIsLoading] = useState(true);

  const expectedImageCount = 5;

  return (
    <div className='gallery'>
      {isLoading && <LoadingSpinner pageSpinner={false} />}
      <img
        src={mainImage}
        alt='individual-product'
        onLoad={() => setIsLoading(false)}
        className={
          isLoading
            ? 'product-image-loading'
            : 'product-image-loaded'
        }
      />
      <div className='gallery__product--images'>
        {[...Array(expectedImageCount)].map((_, index) => {
          return (
            <div key={index} className='gallery__image-container'>
              {isLoading && <LoadingSpinner pageSpinner={false} />}
              <img
                src={images[index]}
                alt='individual-product'
                onClick={(e) => setMainImage(e.target.src)}
                className={
                  isLoading
                    ? 'product-image-loading'
                    : 'product-image-loaded'
                }
              />
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ProductGallery;
