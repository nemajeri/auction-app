import React, { useState, useEffect, useContext } from 'react';
import { useParams } from 'react-router-dom';
import './productOverviewPage.css';
import {
  ProductDetails,
  ProductGallery,
} from '../../components/product-overview-page/index';
import { getProduct } from '../../utils/api/productsApi';
import {
  updateBid,
  getHighestBidForUserAndProduct,
} from '../../utils/api/bidApi';
import { calculateTimeLeft } from '../../utils/helperFunctions';
import './productOverviewPage.css';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import BreadCrumbs from '../../components/breadcrumbs/Breadcrumbs';
import { toast } from 'react-toastify';
import { AppContext } from '../../utils/AppContextProvider';
import { AUCTION_ENDED } from '../../utils/constants';
import { productTabs } from '../../data/tabs';

const ProductOverviewPage = () => {
  const [selectedTab, setSelectedTab] = useState(productTabs[0].id);
  const [product, setProduct] = useState(null);
  const [images, setImages] = useState([]);
  const [timeLeft, setTimeLeft] = useState('');
  const [loading, setLoading] = useState(true);
  const [isOwner, setIsOwner] = useState(null);
  const [bidAmount, setBidAmount] = useState('');
  const [userHighestBid, setUserHighestBid] = useState(null);
  const { id } = useParams();
  const { user } = useContext(AppContext);

  useEffect(() => {
    (async () => {
      try {
        const response = await getProduct(id);
        setProduct(response.data);
        setImages(response.data.images);
        setTimeLeft(calculateTimeLeft(response.data));
        setIsOwner(response.data.owner);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    })();
  }, [id]);

  useEffect(() => {
    if (!isOwner && timeLeft === AUCTION_ENDED) {
      (async () => {
        try {
          const response = await getHighestBidForUserAndProduct(
            user?.id,
            product.id
          );
          setUserHighestBid(response.data);
        } catch (error) {
          console.error(
            'Error fetching highest bid for user and product:',
            error
          );
        } finally {
          setLoading(false);
        }
      })();
    }
    
  }, [user?.id, product, isOwner, timeLeft]);

  const handleTabClick = (id) => {
    setSelectedTab(id);
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  const onBidButtonClick = async (e) => {
    e.preventDefault();
    try {
      await updateBid(id, bidAmount);
      const updatedProduct = await getProduct(id);
      setProduct(updatedProduct.data);

      toast.success('Congrats! You are the highest bidder!');
    } catch (error) {
      let message = 'An error occurred while placing your bid.';

      if (
        error.response &&
        error.response.data &&
        error.response.data.message
      ) {
        const errorMessage = error.response.data.message;
        switch (errorMessage) {
          case 'Place bid that is higher than the current one':
            message =
              'There are higher bids than yours. You could give a second try!';
            toast.warning(message);
            break;
          default:
            toast.error(message);
            break;
        }
      } else {
        toast.error(message);
      }
    } finally {
      setBidAmount('');
    }
  };

  return (
    <>
      <BreadCrumbs title={product?.productName} />
      <div className='wrapper product-overview-page__wrapper'>
        <div className='content'>
          <section className='product-overview-page__gallery--and_details'>
            <ProductGallery images={images} />
            <ProductDetails
              tabs={productTabs}
              handleTabClick={handleTabClick}
              selectedTab={selectedTab}
              product={product}
              timeLeft={timeLeft}
              isOwner={isOwner}
              setBidAmount={setBidAmount}
              onBidButtonClick={onBidButtonClick}
              bidAmount={bidAmount}
              userHighestBid={userHighestBid}
              user={user}
            />
          </section>
        </div>
      </div>
    </>
  );
};

export default ProductOverviewPage;
