import React, { useState, useEffect } from 'react';
import Button from '../../../utils/Button';
import Tabs from '../../tabs/Tabs';
import './productDetails.css';
import Modal from '../../../utils/forms/Modal';
import StripeCheckout from '../../stripe-checkout/StripeCheckout';
import { useNavigate, useLocation } from 'react-router-dom';
import { landingPagePath } from '../../../utils/paths';
import { AUCTION_ENDED } from '../../../utils/constants';

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
  isUserHighestBidder,
  user,
}) => {
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  useEffect(() => {
    if (location.state?.openModal) {
      setShowPaymentModal(true);
    }
  }, [location]);

  const onClose = () => {
    setShowPaymentModal(false);
    navigate(landingPagePath);
  };

  const isAuctionActive = !product?.sold && timeLeft !== AUCTION_ENDED;

  return (
    product && (
      <div className='details'>
        <h1>{product.productName}</h1>
        <p className='details__start--price'>
          Starts from <span>${product.startPrice.toFixed(2)}</span>
        </p>
        <div className='details__offer'>
          <p>
            Highest bid: <span>{product.highestBid.toFixed(2)}$</span>
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
            {isAuctionActive ? (
              <>
                <input
                  type='text'
                  value={bidAmount}
                  placeholder={`Enter ${product.highestBid.toFixed(
                    2
                  )}$ or higher`}
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
            ) : (
              isUserHighestBidder &&
              !isAuctionActive && (
                <Button
                  onClick={() => setShowPaymentModal(true)}
                  className={'details__button pay'}
                  isOwner={isOwner}
                >
                  PAY
                </Button>
              )
            )}
          </div>
        )}
        <Tabs
          tabs={tabs}
          handleTabClick={handleTabClick}
          selectedTab={selectedTab}
        />
        <p className='details__description'>{product.description}</p>
        <Modal showModal={showPaymentModal} onClose={onClose}>
          <h2>Complete your payment</h2>
          <StripeCheckout product={product} />
        </Modal>
      </div>
    )
  );
};

export default ProductDetails;
