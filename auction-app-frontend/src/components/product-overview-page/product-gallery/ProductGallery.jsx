import React, { useState } from 'react';
import './productGallery.css';

const ProductGallery = () => {
  const [mainImage, setMainImage] = useState(
    'https://images.unsplash.com/photo-1491553895911-0055eca6402d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80'
  );

  const handleImageClick = (event) => {
    setMainImage(event.target.src);
  };

  return (
    <div className='gallery'>
      <img
        src={mainImage}
        alt='individual-product'
      />
      <div className='gallery__product--images'>
        <img
          src='https://images.unsplash.com/photo-1626947346165-4c2288dadc2a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80'
          alt='individual-product'
          onClick={handleImageClick}
        />
        <img
          src='https://images.unsplash.com/photo-1491553895911-0055eca6402d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80'
          alt='individual-product'
          onClick={handleImageClick}
        />
        <img
          src='https://images.unsplash.com/photo-1491553895911-0055eca6402d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80'
          alt='individual-product'
          onClick={handleImageClick}
        />
        <img
          src='https://images.unsplash.com/photo-1491553895911-0055eca6402d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80'
          alt='individual-product'
          onClick={handleImageClick}
        />
        <img
          src='https://images.unsplash.com/photo-1491553895911-0055eca6402d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80'
          alt='individual-product'
          onClick={handleImageClick}
        />
      </div>
    </div>
  );
};

export default ProductGallery;
