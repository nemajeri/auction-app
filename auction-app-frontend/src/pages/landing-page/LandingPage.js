import React, { useState, useEffect } from 'react';
import {
  Categories,
  HighlightedProduct,
  LandingPageProducts,
} from '../../components/landing-page/index';
import './landingPage.css';
import {
  getSortedNewAndLastProducts,
} from '../../utils/api/productsApi';
import { getCategories } from '../../utils/api/categoryApi';
import { HIGHLIGHTED_PRODUCT } from '../../utils/constants';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import Tabs from '../../components/tabs/Tabs';

const tabs = [
  { id: 'newArrivals', label: 'New Arrivals', filter: 'new-arrival' },
  { id: 'lastChance', label: 'Last Chance', filter: 'last-chance' },
];

const LandingPage = () => {
  const [selectedTab, setSelectedTab] = useState(tabs[0].id);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const [pageNumber, setPageNumber] = useState(0);

  useEffect(() => {
    const selectedFilter = tabs.find((tab) => tab.id === selectedTab).filter;
    fetchProductsAndCategories(selectedFilter, pageNumber);
  }, [pageNumber, selectedTab]);

  async function fetchProductsAndCategories(filter, pageNumber) {
    let categories = await getCategories();
    setCategories(categories.data);

    let products = await getSortedNewAndLastProducts(filter, pageNumber);
    setProducts(products.data);
    setLoading(false);
  }

  if (loading) {
    return <LoadingSpinner />;
  }

  const handleTabClick = async (id) => {
    setSelectedTab(id);
    const selectedFilter = tabs.find((tab) => tab.id === id).filter;
    const response = await getSortedNewAndLastProducts(selectedFilter, 0);
    setProducts(response.data);
  };

  const fetchNextPage = async () => {
    setLoading(true);
    const selectedFilter = tabs.find((tab) => tab.id === selectedTab).filter;
    const response = await getSortedNewAndLastProducts(selectedFilter, pageNumber);
    if (response.data.length < 8) {
      setHasMore(false);
    } else {
      setHasMore(true);
    }
    setProducts((prevProducts) => [...prevProducts, ...response.data]);
    setPageNumber((prevPageNumber) => prevPageNumber + 1);
    setLoading(false);
  };

  return (
    <div className='wrapper landing-page__wrapper'>
      <div className='content'>
        <section className='landing-page__categories--and_product'>
          <Categories categories={categories} />
          {!loading && (
            <HighlightedProduct highlightedProduct={HIGHLIGHTED_PRODUCT} />
          )}
        </section>
        <Tabs
          selectedTab={selectedTab}
          handleTabClick={handleTabClick}
          tabs={tabs}
        />
        <LandingPageProducts
          products={products}
          fetchNextPage={fetchNextPage}
          hasMore={hasMore}
        />
      </div>
    </div>
  );
};

export default LandingPage;
