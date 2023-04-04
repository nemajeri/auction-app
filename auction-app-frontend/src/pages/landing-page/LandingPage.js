import React, { useState, useEffect, useRef } from 'react';
import {
  Categories,
  HighlightedProduct,
  LandingPageProducts,
} from '../../components/landing-page/index';
import './landingPage.css';
import { getSortedNewAndLastProducts } from '../../utils/api/productsApi';
import { getAllProducts } from '../../utils/api/productsApi';
import { getCategories } from '../../utils/api/categoryApi';
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
  const [highlightedProducts, setHighlightedProducts] = useState([]);
  const currentPageNumber = useRef(0);

  const uniqueProducts = Array.from(
    new Set(products?.map((product) => product.id))
  ).map((productId) => products?.find((product) => product.id === productId));

  useEffect(() => {
    const selectedFilter = tabs.find((tab) => tab.id === selectedTab).filter;
    fetchProductsAndCategories(selectedFilter, currentPageNumber.current);
  }, [selectedTab]);

  async function fetchProductsAndCategories(filter, currentPageNumber) {
    let categories = await getCategories();
    setCategories(categories.data);

    let sortedProducts = await getSortedNewAndLastProducts(
      filter,
      currentPageNumber
    );

    let allProducts = await getAllProducts();
    setHighlightedProducts(
      allProducts.data.filter((product) => product.highlighted === true)
    );
    setProducts(sortedProducts.data);
    setLoading(false);
  }

  const handleTabClick = async (id) => {
    setLoading(true);
    setSelectedTab(id);
    currentPageNumber.current = 0;
    setHasMore(true);
    const selectedFilter = tabs.find((tab) => tab.id === id).filter;
    const response = await getSortedNewAndLastProducts(selectedFilter, 0);
    setProducts(response.data);
    setLoading(false);
  };

  const fetchNextPage = async () => {
    setLoading(true);
    const selectedFilter = tabs.find((tab) => tab.id === selectedTab).filter;

    currentPageNumber.current += 1;

    setTimeout(() => {
      getSortedNewAndLastProducts(selectedFilter, currentPageNumber.current)
        .then((response) => {
          if (response.data.length === 0) {
            setHasMore(false);
          } else {
            setHasMore(true);
            setProducts((prevProducts) => prevProducts.concat(response.data));
          }
          setLoading(false);
        })
        .catch((error) => {
          console.error('Error fetching next page:', error);
          setLoading(false);
        });
    }, 500);
  };

  return (
    <div className='wrapper landing-page__wrapper'>
      <div className='content'>
        <section className='landing-page__categories--and_product'>
          <Categories categories={categories} />
          {!loading && (
            <HighlightedProduct highlightedProduct={highlightedProducts[0]} />
          )}
        </section>
        <Tabs
          selectedTab={selectedTab}
          handleTabClick={handleTabClick}
          tabs={tabs}
        />
        <LandingPageProducts
          products={uniqueProducts}
          fetchNextPage={fetchNextPage}
          hasMore={hasMore}
          loading={loading}
        />
      </div>
    </div>
  );
};

export default LandingPage;
