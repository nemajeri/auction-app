import React, { useContext } from 'react';
import Button from '../../../utils/Button';
import Tabs from '../../tabs/Tabs';
import './productDetails.css';
import { AppContext } from '../../../utils/AppContextProvider';

const ProductDetails = ({
  tabs,
  handleTabClick,
  selectedTab,
  timeLeft,
  product,
  isOwner,
  setBidAmount,
  bidAmount,
  onBidButtonClick,
  isAuctionOver,
}) => {
  const { user } = useContext(AppContext);
  return (
    product && (
      <div className='details'>
        <h1>{product.productName}</h1>
        <p className='details__start--price'>
          Starts from <span>${product.startPrice}</span>
        </p>
        <div className='details__offer'>
          <p>
            Highest bid: <span>{product.highestBid}$</span>
          </p>
          <p>
            Number of bids: <span>{product.numberOfBids}</span>
          </p>
          <p>
            Time left: <span>{timeLeft}</span>
          </p>
        </div>
        {user && !isAuctionOver && (
          <div className='details__bid--placement'>
            <input
              type='text'
              value={bidAmount}
              placeholder={`Enter ${product.highestBid}$ or higher`}
              disabled={isOwner}
              onChange={(e) => setBidAmount(e.target.value)}
            />
            <Button
              onClick={onBidButtonClick}
              className={'details__button'}
              isOwner={isOwner}
            >
              PLACE BID
            </Button>
          </div>
        )}
        <Tabs
          tabs={tabs}
          handleTabClick={handleTabClick}
          selectedTab={selectedTab}
        />
        <p className='details__description'>{product.description}</p>
      </div>
    )
  );
};

export default ProductDetails;
