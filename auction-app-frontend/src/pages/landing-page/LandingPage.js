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
import { AppContext } from '../../utils/AppContextProvider';
import RecommendedProducts from '../../components/landing-page/recommended-products/RecommendedProducts.jsx';
import { useGridView } from '../../hooks/useGridView';
import { getStartOfTodayUTC } from '../../utils/helperFunctions';
import moment from 'moment';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import { usePageLoading } from '../../hooks/usePageLoading';

import { landingPageProductClassName } from '../../utils/styles';
import { homeTabs } from '../../data/tabs';

const LandingPage = () => {
  const [selectedTab, setSelectedTab] = useState(homeTabs[0].id);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const [highlightedProducts, setHighlightedProducts] = useState([]);
  const [recommendedProducts, setRecommendedProducts] = useState([]);
  const currentPageNumber = useRef(0);
  const { user, initialLoading } = useContext(AppContext);
  const GridViewRecommendedProducts = useGridView(RecommendedProducts);
  useEffect(() => {
    const selectedFilter = homeTabs.find(
      (tab) => tab.id === selectedTab
    ).filter;
    fetchProductsAndCategories(selectedFilter, currentPageNumber.current);
  }, [selectedTab]);

  usePageLoading();

  useEffect(() => {
    (async () => {
      if (user) {
        try {
          const response = await getRecommendedProducts(user.id);
          setRecommendedProducts(response);
        } catch (error) {
          console.error('Error fetching recommended products:', error);
        }
      }
    })();
  }, [user]);

  const fetchProductsAndCategories = async (filter, currentPageNumber) => {
    let categories = await getCategories();
    setCategories(categories.data);

    let sortedProducts = await getSortedProductsAccordingToDate(
      filter,
      currentPageNumber
    );

    let allProducts = await getAllProductsToSeparateHighlighted();
    let highlightedProducts = allProducts?.data?.filter((product) => {
      const endDate = moment(product.endDate);
      return product.highlighted === true && getStartOfTodayUTC() <= endDate;
    });
    setHighlightedProducts(highlightedProducts);
    setProducts(sortedProducts.data.content);
    setLoading(false);
  };

  const handleTabClick = async (id) => {
    setLoading(true);
    setSelectedTab(id);
    currentPageNumber.current = 0;
    setHasMore(true);

    setProducts([]);

    const selectedFilter = homeTabs.find((tab) => tab.id === id).filter;
    const response = await getSortedProductsAccordingToDate(selectedFilter, 0);

    setProducts(response.data.content);
    setLoading(false);
  };

  const fetchNextPage = async () => {
    const selectedFilter = homeTabs.find(
      (tab) => tab.id === selectedTab
    ).filter;

    currentPageNumber.current += 1;

    const response = await getSortedProductsAccordingToDate(
      selectedFilter,
      currentPageNumber.current
    );
    setProducts((prevProducts) => [...prevProducts, ...response.data.content]);
  };

  if (initialLoading) {
    return <LoadingSpinner pageSpinner={true} />;
  }

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
          tabs={homeTabs}
        />
        <LandingPageProducts
          products={products}
          fetchNextPage={fetchNextPage}
          hasMore={hasMore}
          landingPageProductClassName={landingPageProductClassName}
        />
      </div>
    </div>
  );
};

export default LandingPage;
