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
import PopOut from '../../components/pop-out/PopOut';
import { AppContext } from '../../utils/AppContextProvider';

const tabs = [{ id: 'details', label: 'Details' }];

const ProductOverviewPage = () => {
  const [selectedTab, setSelectedTab] = useState(tabs[0].id);
  const [product, setProduct] = useState(null);
  const [images, setImages] = useState([]);
  const [timeLeft, setTimeLeft] = useState('');
  const [loading, setLoading] = useState(true);
  const [isOwner, setIsOwner] = useState(null);
  const [bidAmount, setBidAmount] = useState('');
  const [userHighestBid, setUserHighestBid] = useState(null);
  const [popOut, setPopOut] = useState({
    visible: false,
    message: '',
    type: '',
  });
  const { id } = useParams();
  const { user } = useContext(AppContext);

  useEffect(() => {
    try {
      getProduct(id)
        .then((response) => {
          setProduct(response.data);
          setImages(response.data.images);
          setTimeLeft(calculateTimeLeft(response.data));
          setLoading(false);
          setIsOwner(response.data.owner);
        })
        .catch((error) => {
          console.error(error);
          setLoading(false);
        });
    } catch (error) {
      console.error(error);
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (user && product) {
      getHighestBidForUserAndProduct(user.id, product.id)
        .then((response) => {
          setUserHighestBid(response.data);
        })
        .catch((error) => {
          console.error(
            'Error fetching highest bid for user and product:',
            error
          );
        });
    }
  }, [user, product]);

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

      setPopOut({
        visible: true,
        message: 'Congrats! You are the highest bidder!',
        type: 'success',
      });
    } catch (error) {
      let message = 'An error occurred while placing your bid.';
      let type = 'error';

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
            type = 'warning';
            break;
          default:
            break;
        }
      }

      setPopOut({
        visible: true,
        message,
        type,
      });
    } finally {
      setTimeout(() => {
        setPopOut({ visible: false, message: '', type: '' });
      }, 2000);
      setBidAmount('');
    }
  };

  return (
    <>
      <BreadCrumbs title={product?.productName} />
      <PopOut popOut={popOut} />
      <div className='wrapper product-overview-page__wrapper'>
        <div className='content'>
          <section className='product-overview-page__gallery--and_details'>
            <ProductGallery images={images} />
            <ProductDetails
              tabs={tabs}
              handleTabClick={handleTabClick}
              selectedTab={selectedTab}
              product={product}
              timeLeft={timeLeft}
              isOwner={isOwner}
              setBidAmount={setBidAmount}
              onBidButtonClick={onBidButtonClick}
              bidAmount={bidAmount}
              userHighestBid={userHighestBid}
            />
          </section>
        </div>
      </div>
    </>
  );
};

export default ProductOverviewPage;
