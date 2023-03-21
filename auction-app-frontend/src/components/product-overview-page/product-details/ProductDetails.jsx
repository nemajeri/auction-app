import React from 'react';
import Button from '../../../utils/Button';
import Tabs from '../../tabs/Tabs';
import './productDetails.css';

const ProductDetails = ({ tabs, handleTabClick, selectedTab }) => {
  const handleButtonClicked = () => {
    console.log('clicked');
  };

  return (
    <div className='details'>
      <h1>BIYLACLESEN Womens 3-in-1 Snowboard Jacket Winter Coats</h1>
      <p className='details__start--price'>
        Starts from <span>$50</span>
      </p>
      <div className='details__offer'>
        <p>
          Highest bid: <span>50$</span>
        </p>
        <p>
          Number of bids: <span>50</span>
        </p>
        <p>
          Time left: <span>10 days 6 hours</span>
        </p>
      </div>
      <div className='details__bid--placement'>
        <input type='text' placeholder='Enter 55$ or higher' />
        <Button onClick={handleButtonClicked} className={'details__button'}>
          PLACE BID
        </Button>
      </div>
      <Tabs
        tabs={tabs}
        handleTabClick={handleTabClick}
        selectedTab={selectedTab}
      />
      <p className='details__description'>
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
        tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim
        veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea
        commodo consequat. Duis aute irure dolor in reprehenderit in voluptate
        velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint
        occaecat cupidatat non proident, sunt in culpa qui officia deserunt
        mollit anim id est laborum.
      </p>
    </div>
  );
};

export default ProductDetails;
