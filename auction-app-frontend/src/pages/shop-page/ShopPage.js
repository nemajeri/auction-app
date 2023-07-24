import React, { useState, useEffect, useContext, useCallback, useRef } from 'react';
import CategoriesAccordion from '../../components/shop-page/categories-accordion/CategoriesAccordion';
import ShopPageProducts from '../../components/shop-page/shop-page-products/ShopPageProducts';
import { useGridView } from '../../hooks/useGridView';
import Button from '../../utils/Button';
import { getAllProducts } from '../../utils/api/productsApi';
import { AppContext } from '../../utils/AppContextProvider';
import sortingField from '../../data/options';
import { useParams } from 'react-router-dom';
import { PAGE_SIZE, ALL_CATEGORIES_ID } from '../../utils/constants';
import { ACTIONS } from '../../utils/appReducer';
import { usePageLoading } from '../../hooks/usePageLoading';
import SelectField from '../../utils/forms/SelectField';

import './shopPage.css';

const ShopPage = () => {
  const {
    searchTerm,
    sortBy,
    activeCategory,
    totalPages,
    dispatch,
    products,
    categories
  } = useContext(AppContext);
  const GridViewProducts = useGridView(ShopPageProducts);
  const [loading, setLoading] = useState(false);
  const currentPageNumber = useRef(0);
  const { categoryId } = useParams();

  usePageLoading();

  const handleOpeningAndFetchingAccordingToCategory = useCallback(
    (categoryId) => async () => {
      dispatch({ type: ACTIONS.SET_ACTIVE_CATEGORY, payload: categoryId });
      try {
        setLoading(true);
        const response = await getAllProducts(
          currentPageNumber.current = 0,
          PAGE_SIZE,
          searchTerm,
          categoryId,
          sortBy
        );
        dispatch({
          type: ACTIONS.SET_INITIAL_PRODUCTS,
          payload: response.data.content,
        });
        dispatch({
          type: ACTIONS.SET_TOTAL_PAGES,
          payload: response.data.totalPages - 1,
        });
      } catch (error) {
        console.error(
          'Error during fetching of products according to categories'.error
        );
        throw error;
      } finally {
        setLoading(false);
      }
    },
    [searchTerm, sortBy, dispatch]
  );
  
  const onExploreMoreBtnClick = () => {
    currentPageNumber.current += 1;

    getAllProducts(currentPageNumber.current, PAGE_SIZE, searchTerm, activeCategory)
      .then((response) => {
        const { content } = response.data;
        dispatch({
          type: ACTIONS.SET_PRODUCTS,
          payload: content,
        });
      })
      .catch((error) => {
        console.error(error);
      });
  };

  useEffect(() => {
    if (categoryId) {
      handleOpeningAndFetchingAccordingToCategory(categoryId)();
    } else if (activeCategory) {
      handleOpeningAndFetchingAccordingToCategory(activeCategory)();
    } else {
      handleOpeningAndFetchingAccordingToCategory(ALL_CATEGORIES_ID)();
    }
    // eslint-disable-next-line
  }, [handleOpeningAndFetchingAccordingToCategory, categoryId]);

  return (
    <div className='wrapper shop-page__wrapper'>
      <div className='container'>
        <div className='shop-page__content'>
          <CategoriesAccordion
            openedCategoryId={activeCategory}
            categories={categories}
            handleOpeningAndFetchingAccordingToCategory={
              handleOpeningAndFetchingAccordingToCategory
            }
          />
          <div className='shop-page__products'>
            <SelectField field={sortingField} dispatch={dispatch} />
            {products && products?.content?.length === 0 ? (
              <div className='shop-page__no-products'>
                <h4>No products found.</h4>
                <p>Please try a different search or filter by category.</p>
              </div>
            ) : (
              <>
                <GridViewProducts
                  className={'shop-page__grid-view'}
                  products={products}
                  currentLocation={'shop'}
                  loading={loading}
                />
              </>
            )}
            {currentPageNumber.current < totalPages && (
              <Button
                Icon={null}
                onClick={onExploreMoreBtnClick}
                className={'shop-page__explore--more_button'}
                iconClassName={null}
              >
                Explore more
              </Button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ShopPage;
