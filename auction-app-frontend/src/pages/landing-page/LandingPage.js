import React, { useState, useEffect, useRef, useContext, useCallback } from 'react';
import {
  Categories,
  HighlightedProduct,
  LandingPageProducts,
} from '../../components/landing-page/index';
import './landingPage.css';
import {
  getSortedProductsAccordingToDate,
  getAllHighlightedProducts,
  getRecommendedProducts,
} from '../../utils/api/productsApi';
import Tabs from '../../components/tabs/Tabs';
import { AppContext } from '../../utils/AppContextProvider';
import RecommendedProducts from '../../components/landing-page/recommended-products/RecommendedProducts.jsx';
import { useGridView } from '../../hooks/useGridView';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import { usePageLoading } from '../../hooks/usePageLoading';

import { landingPageProductClassName } from '../../utils/styles';
import { homeTabs } from '../../data/tabs';
import { ACTIONS } from '../../utils/appReducer';

const LandingPage = () => {
  const [selectedTab, setSelectedTab] = useState(homeTabs[0].id);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(true);
  const [highlightedProducts, setHighlightedProducts] = useState([]);
  const [recommendedProducts, setRecommendedProducts] = useState([]);
  const currentPageNumber = useRef(0);
  const { user, initialLoading, products, categories, dispatch } =
    useContext(AppContext);
  const GridViewRecommendedProducts = useGridView(RecommendedProducts);

  const fetchProducts = useCallback(async (filter, currentPageNumber) => {
    try {

      let sortedProducts = await getSortedProductsAccordingToDate(
        filter,
        currentPageNumber
      );

      let highlightedProducts = await getAllHighlightedProducts();

      setHighlightedProducts(highlightedProducts.data);
      dispatch({type: ACTIONS.SET_INITIAL_PRODUCTS ,payload: sortedProducts.data.content});
      setLoading(false);
    } catch (error) {
      console.error(error);
    }
  }, [dispatch]);


  useEffect(() => {
    const selectedFilter = homeTabs.find(
      (tab) => tab.id === selectedTab
    ).filter;
    fetchProducts(selectedFilter, currentPageNumber.current);
  }, [selectedTab, fetchProducts]);

  usePageLoading();

  useEffect(() => {
    (async () => {
      if (user) {
        try {
          const response = await getRecommendedProducts();
          setRecommendedProducts(response);
        } catch (error) {
          console.error('Error fetching recommended products:', error);
        }
      }
    })();
  }, [user]);

  const handleTabClick = async (id) => {
    setLoading(true);
    setSelectedTab(id);
    currentPageNumber.current = 0;
    setHasMore(true);

    dispatch({type: ACTIONS.SET_INITIAL_PRODUCTS ,payload: []});

    const selectedFilter = homeTabs.find((tab) => tab.id === id).filter;
    const response = await getSortedProductsAccordingToDate(selectedFilter, 0);

    dispatch({type: ACTIONS.SET_INITIAL_PRODUCTS ,payload: response.data.content});
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
    dispatch({type: ACTIONS.SET_PRODUCTS ,payload: response.data.content});
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
