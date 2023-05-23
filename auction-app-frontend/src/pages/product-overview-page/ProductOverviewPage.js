import React, { useState, useEffect, useContext } from 'react';
import { useParams } from 'react-router-dom';
import './productOverviewPage.css';
import {
  ProductDetails,
  ProductGallery,
} from '../../components/product-overview-page/index';
import { getProduct } from '../../utils/api/productsApi';
import { updateBid } from '../../utils/api/bidApi';
import { calculateTimeLeft } from '../../utils/helperFunctions';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import BreadCrumbs from '../../components/breadcrumbs/Breadcrumbs';
import { toast } from 'react-toastify';
import { AppContext } from '../../utils/AppContextProvider';
import { EMPTY_STRING } from '../../utils/constants';
import { productTabs } from '../../data/tabs';
import { usePageLoading } from '../../hooks/usePageLoading';

const ProductOverviewPage = () => {
  const [selectedTab, setSelectedTab] = useState(productTabs[0].id);
  const [product, setProduct] = useState(null);
  const [images, setImages] = useState([]);
  const [timeLeft, setTimeLeft] = useState(EMPTY_STRING);
  const [loading, setLoading] = useState(true);
  const [isOwner, setIsOwner] = useState(null);
  const [bidAmount, setBidAmount] = useState(EMPTY_STRING);
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
        setUserHighestBid(response.data.userHighestBid);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    })();
  }, [id]);

  usePageLoading();

  const handleTabClick = (id) => {
    setSelectedTab(id);
  };

  if (loading) {
    return <LoadingSpinner pageSpinner={true} />;
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
        if (errorMessage === 'Place bid that is higher than the current one') {
          message = 'There are higher bids than yours. You could give a second try!';
          toast.warning(message);
        } else {
          toast.error(message);
        }
      } else {
        toast.error(message);
      }
    } finally {
      setBidAmount(EMPTY_STRING);
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
