import React, { useState, useEffect, useRef, useContext } from 'react';
import {
  Categories,
  HighlightedProduct,
  LandingPageProducts,
} from '../../components/landing-page/index';
import './landingPage.css';
import {
  getSortedProductsAccordingToDate,
  getAllProductsToSeparateHighlighted,
  getRecommendedProducts,
} from '../../utils/api/productsApi';
import { getCategories } from '../../utils/api/categoryApi';
import Tabs from '../../components/tabs/Tabs';
import { LANDING_PAGE_SIZE } from '../../utils/constants';
import { AppContext } from '../../utils/AppContextProvider';
import RecommendedProducts from '../../components/landing-page/recommended-products/RecommendedProducts.jsx';
import { useGridView } from '../../hooks/useGridView';
import { getTodayWithoutTime } from '../../utils/helperFunctions';

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
  const [recommendedProducts, setRecommendedProducts] = useState([]);
  const currentPageNumber = useRef(0);
  const { user } = useContext(AppContext);
  const GridViewRecommendedProducts = useGridView(RecommendedProducts);

  useEffect(() => {
    const selectedFilter = tabs.find((tab) => tab.id === selectedTab).filter;
    fetchProductsAndCategories(selectedFilter, currentPageNumber.current);
  }, [selectedTab]);

  useEffect(() => {
    const fetchRecommendedProducts = async () => {
      if (user) {
        try {
          const response = await getRecommendedProducts(user.id);
          setRecommendedProducts(response);
        } catch (error) {
          console.error('Error fetching recommended products:', error);
        }
      }
    };

    fetchRecommendedProducts();
  }, [user]);

  async function fetchProductsAndCategories(filter, currentPageNumber) {
    let categories = await getCategories();
    setCategories(categories.data);

    let sortedProducts = await getSortedProductsAccordingToDate(
      filter,
      currentPageNumber
    );

    let allProducts = await getAllProductsToSeparateHighlighted();
    let highlightedProducts = allProducts?.data?.filter((product) => {
      const endDate = new Date(product.endDate);
      return product.highlighted === true && getTodayWithoutTime().getTime() <= endDate.getTime();
    })
    setHighlightedProducts(highlightedProducts);
    setProducts(sortedProducts.data.content);
    setLoading(false);
  }

  const handleTabClick = async (id) => {
    setLoading(true);
    setSelectedTab(id);
    currentPageNumber.current = 0;
    setHasMore(true);

    setProducts([]);

    const selectedFilter = tabs.find((tab) => tab.id === id).filter;
    const response = await getSortedProductsAccordingToDate(selectedFilter, 0);

    setProducts(response.data.content);
    setLoading(false);
  };

  const fetchNextPage = async () => {
    setLoading(true);
    const selectedFilter = tabs.find((tab) => tab.id === selectedTab).filter;

    if (products.length < LANDING_PAGE_SIZE) {
      setLoading(false);
      return;
    }

    currentPageNumber.current += 1;

    const response = await getSortedProductsAccordingToDate(
      selectedFilter,
      currentPageNumber.current
    );
    setProducts((prevProducts) => [...prevProducts, ...response.data.content]);
    setLoading(false);
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
        {user && (
          <>
            <Tabs
              tabs={[
                {
                  id: 'recommendedProducts',
                  label: 'Recommended Products',
                  filter: 'recommended-products',
                },
              ]}
              disableClick
              labelClassName='landing-page__recommended--products_tab'
            />
            <GridViewRecommendedProducts
              products={recommendedProducts}
              fetchNextPage={fetchNextPage}
              hasMore={hasMore}
              loading={loading}
              className={'landing-page__recommended-products_grid-view'}
            />
          </>
        )}
        <Tabs
          selectedTab={selectedTab}
          handleTabClick={handleTabClick}
          tabs={tabs}
        />
        <LandingPageProducts
          products={products}
          fetchNextPage={fetchNextPage}
          hasMore={hasMore}
          loading={loading}
        />
      </div>
    </div>
  );
};

export default LandingPage;
