import React, { useContext, useState } from 'react';
import Button from '../../../utils/Button';
import Tabs from '../../tabs/Tabs';
import './productDetails.css';
import { AppContext } from '../../../utils/AppContextProvider';
import Modal from '../../../utils/forms/Modal';
import StripeCheckout from '../../stripe-checkout/StripeCheckout';

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
  userHighestBid,
}) => {
  const { user } = useContext(AppContext);
  const [showPaymentModal, setShowPaymentModal] = useState(false);

  const onPayButtonClick = () => {
    setShowPaymentModal(true);
  };

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
        {user && (
          <div className='details__bid--placement'>
            {timeLeft !== 'Auction ended' ? (
              <>
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
              </>
            ) : userHighestBid === product.highestBid ? (
              <Button
                onClick={onPayButtonClick}
                className={'details__button pay'}
                isOwner={isOwner}
              >
                PAY
              </Button>
            ) : null}
          </div>
        )}
        <Tabs
          tabs={tabs}
          handleTabClick={handleTabClick}
          selectedTab={selectedTab}
        />
        <p className='details__description'>{product.description}</p>
        <Modal showModal={showPaymentModal} closePath={'/'}>
          <h2>Complete your payment</h2>
          <StripeCheckout product={product} />
        </Modal>
      </div>
    )
  );
};

export default ProductDetails;
