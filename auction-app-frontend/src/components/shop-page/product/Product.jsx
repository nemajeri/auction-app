import React from 'react';
import './product.css';
import Button from '../../../utils/Button';
import { CiHeart } from 'react-icons/ci';
import { AiOutlineDollarCircle } from 'react-icons/ai';

const Product = () => {
  const onWishlistBtnClick = () => {
    console.log('Clicked on wishlist button');
  };

  const onBidBtnClick = () => {
    console.log('Clicked on bid button');
  };

  return (
    <div className='product'>
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
          src='https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80'
          alt='products'
        />
      </div>
      <div className='product__details'>
        <h3>Shoe Collection</h3>
        <p>
          Start From &nbsp;<span>$59.99</span>
        </p>
      </div>
    </div>
  );
};

export default Product;
