import React, { useState, useEffect } from 'react';
// import { useParams } from 'react-router-dom';
import './productOverviewPage.css';
import {
  ProductDetails,
  ProductGallery,
} from '../../components/product-overview-page/index';
import { getProduct } from '../../utils/api/productsApi';
import { calculateTimeLeft } from '../../utils/helperFunctions';
import './productOverviewPage.css';

const tabs = [{ id: 'details', label: 'Details' }];

const ProductOverviewPage = () => {
  const [selectedTab, setSelectedTab] = useState(tabs[0].id);
  const [product, setProduct] = useState(null);
  const [images, setImages] = useState([]);
  const [timeLeft, setTimeLeft] = useState('');
  const [loading, setLoading] = useState(true);
  // const { id } = useParams();
  const id = 1;


  useEffect(() => {
    getProduct(id)
      .then((response) => {
        setProduct(response.data);
        setImages(response.data.images.replace(/[{}]/g, '').split(',').map(str => str.replace(/['"]+/g, '')));
        setTimeLeft(calculateTimeLeft(response.data));
        setLoading(false);
      })
      .catch((error) => {
        console.error(error);
        setLoading(false);
      });
  }, []);

  const handleTabClick = (id) => {
    setSelectedTab(id);
  };

  return (
    <div className='wrapper'>
      <div className='content'>
        <section className='product-overview-page__gallery--and_details'>
          <ProductGallery images={images} />
          <ProductDetails
            tabs={tabs}
            handleTabClick={handleTabClick}
            selectedTab={selectedTab}
            product={product}
            timeLeft={timeLeft}
          />
        </section>
      </div>
    </div>
  );
};

export default ProductOverviewPage;
