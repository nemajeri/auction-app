import React from 'react';
import './product.css';
import Button from '../../../utils/Button';
import { CiHeart } from 'react-icons/ci';
import { AiOutlineDollarCircle } from 'react-icons/ai';

const Product = ({ product:{id, productName, startPrice, images} }) => {
  const onWishlistBtnClick = () => {
    console.log('Clicked on wishlist button');
  };

  const onBidBtnClick = () => {
    console.log('Clicked on bid button');
  };

  const productImages = images.replace(/[{}]/g, '').split(',').map(str => str.replace(/['"]+/g, ''));


  return (
    <div className='product' key={id}>
      <div className='product__img--container'>
        <div className='product__img--overlay'>
          <div>
            <Button
              onClick={onWishlistBtnClick}
              Icon={CiHeart}
              className={'product__img--button'}
              iconClassName={'product__img--button_icon'}
            >
              Wishlist
            </Button>
            <Button
              onClick={onBidBtnClick}
              Icon={AiOutlineDollarCircle}
              className={'product__img--button'}
              iconClassName={'product__img--button_icon'}
            >
              Bid
            </Button>
          </div>
        </div>
        <img
          src={productImages[0]}
          alt='products'
        />
      </div>
      <div className='product__details'>
        <h3>{productName}</h3>
        <p>
          Start From &nbsp;<span>${startPrice}</span>
        </p>
      </div>
    </div>
  );
};

export default Product;
