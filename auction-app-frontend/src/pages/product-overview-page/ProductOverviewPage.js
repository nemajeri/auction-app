import React, { useState } from 'react';
import './productOverviewPage.css';
import {
  ProductDetails,
  ProductGallery,
} from '../../components/product-overview-page/index';
import './productOverviewPage.css';

const tabs = [
  { id: 'details', label: 'Details' },
];

const ProductOverviewPage = () => {
  const [selectedTab, setSelectedTab] = useState(tabs[0].id);

  const handleTabClick = (id) => {
    setSelectedTab(id);
  };

  return (
    <div className='wrapper'>
      <div className='content'>
        <section className='product-overview-page__gallery--and_details'>
          <ProductGallery />
          <ProductDetails
            tabs={tabs}
            handleTabClick={handleTabClick}
            selectedTab={selectedTab}
          />
        </section>
      </div>
    </div>
  );
};

export default ProductOverviewPage;
