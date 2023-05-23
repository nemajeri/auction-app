import React, { useState, useEffect, useContext, useCallback } from 'react';
import './shopPage.css';
import CategoriesAccordion from '../../components/shop-page/categories-accordion/CategoriesAccordion';
import ShopPageProducts from '../../components/shop-page/shop-page-products/ShopPageProducts';
import { useGridView } from '../../hooks/useGridView';
import Button from '../../utils/Button';
import { getCategories } from '../../utils/api/categoryApi';
import { getAllProducts } from '../../utils/api/productsApi';
import { AppContext } from '../../utils/AppContextProvider';
import { getTotalPages } from '../../utils/helperFunctions';
import { useParams } from 'react-router-dom';
import SelectField from '../../utils/forms/SelectField';
import {
  FIELD_NAME,
  FIELD_PLACEHOLDER,
  SORT_OPTIONS,
  PAGE_SIZE,
  ALL_CATEGORIES_ID,
  EMPTY_STRING,
} from '../../utils/constants';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import { usePageLoading } from '../../hooks/usePageLoading';
import { ACTIONS } from '../../utils/appReducer';
import axios from 'axios';

const field = {
  name: FIELD_NAME,
  placeholder: FIELD_PLACEHOLDER,
  options: [
    { label: 'Default sorting', value: SORT_OPTIONS.DEFAULT_SORTING },
    { label: 'Sort By Newness', value: SORT_OPTIONS.START_DATE },
    { label: 'Sort By Time Left', value: SORT_OPTIONS.END_DATE },
    {
      label: 'Sort By Price: Low to High',
      value: SORT_OPTIONS.PRICE_LOW_TO_HIGH,
    },
    {
      label: 'Sort By Price: High to Low',
      value: SORT_OPTIONS.PRICE_HIGH_TO_LOW,
    },
  ],
};

const ShopPage = () => {
  const {
    dispatch,
    searchTerm,
    searchedProducts,
    pageNumber,
    activeCategory,
    products,
    isClearButtonPressed,
    initialLoading,
    currentSortOption,
  } = useContext(AppContext);
  const GridViewProducts = useGridView(ShopPageProducts);
  const [openedCategory, setOpenedCategory] = useState({});
  const [categories, setCategories] = useState([]);
  const [productsByCategories, setProductsByCategories] = useState({
    content: [],
    totalElements: 0,
  });
  const [loading, setLoading] = useState(false);
  const { categoryId } = useParams();

  usePageLoading();

  useEffect(() => {
    const resetStatesOnUnmount = () => {
      dispatch({ type: ACTIONS.SET_SEARCH_TERM, payload: EMPTY_STRING });
      setProductsByCategories({ content: [], totalElements: 0 });
      dispatch({ type: ACTIONS.SET_SEARCHED_PRODUCTS, payload: null });
      dispatch({ type: ACTIONS.SET_PRODUCTS, payload: [] });
    };

    return () => {
      resetStatesOnUnmount();
    };
  }, [dispatch, setProductsByCategories]);

  useEffect(() => {
    const CancelToken = axios.CancelToken;
    const source = CancelToken.source();

    (async () => {
      setLoading(true);
      try {
        const response = await getCategories(source.token);
        setCategories(response.data);
      } catch (error) {
        if (axios.isCancel(error)) {
          console.error('Request cancelled:', error.message);
          return;
        } else {
          console.error('Error fetching categories: ' + error);
        }
      } finally {
        setLoading(false);
      }
    })();

    return () => {
      source.cancel('Operation cancelled by the user.');
    };
  }, []);

  const handleOpeningAndFetchingCategories = useCallback(
    (categoryId) => async (event) => {
      const category = event?.currentTarget.dataset.category;
      const isOpening = category ? !openedCategory[category] : true;

      if (!event && isOpening && category) {
        setOpenedCategory((prevState) => ({
          ...prevState,
          [category]: false,
        }));
        dispatch({ type: ACTIONS.SET_ACTIVE_CATEGORY, payload: null });
        return;
      }

      try {
        setLoading(true);
        dispatch({ type: ACTIONS.SET_PAGE_NUMBER, payload: 0 });

        const productsResponse = await getAllProducts(
          0,
          PAGE_SIZE,
          searchTerm,
          categoryId === ALL_CATEGORIES_ID ? null : categoryId,
          currentSortOption
        );

        const { content, totalElements } = productsResponse.data;

        if (!isOpening && category) {
          if (searchedProducts) {
            dispatch({
              type: ACTIONS.SET_PRODUCTS,
              payload: searchedProducts.content,
            });
          } else {
            dispatch({ type: ACTIONS.SET_PRODUCTS, payload: [] });
          }
        } else {
          if (searchedProducts) {
            const filteredItems = searchedProducts.content.filter(
              (product) => product.categoryId === categoryId
            );
            dispatch({ type: ACTIONS.SET_PRODUCTS, payload: filteredItems });
          } else {
            dispatch({ type: ACTIONS.SET_PRODUCTS, payload: content });
          }
        }

        setProductsByCategories({ content, totalElements });
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }

      if (category) {
        setOpenedCategory((prevState) => {
          const updatedState = Object.keys(prevState).reduce(
            (acc, categoryName) => {
              acc[categoryName] = false;
              return acc;
            },
            {}
          );

          return {
            ...updatedState,
            [category]: isOpening,
          };
        });
        dispatch({
          type: ACTIONS.SET_ACTIVE_CATEGORY,
          payload: isOpening ? categoryId : null,
        });
      }
    },
    [searchTerm, currentSortOption, openedCategory, searchedProducts, dispatch]
  );

  useEffect(() => {
    if (!loading && searchedProducts) {
      dispatch({
        type: ACTIONS.SET_PRODUCTS,
        payload: searchedProducts.content,
      });
    }

    if (isClearButtonPressed) {
      handleOpeningAndFetchingCategories(activeCategory)();
      dispatch({ type: ACTIONS.SET_CLEAR_BUTTON_PRESSED, payload: false });
    }
  }, [
    loading,
    searchedProducts,
    isClearButtonPressed,
    dispatch,
    activeCategory,
    handleOpeningAndFetchingCategories,
  ]);

  useEffect(() => {
    if (categoryId && categories) {
      const firstLoadedCategory = categories.find(
        (category) => category.id === parseInt(categoryId)
      );

      if (firstLoadedCategory) {
        setOpenedCategory({ [firstLoadedCategory.categoryName]: true });
        dispatch({
          type: ACTIONS.SET_ACTIVE_CATEGORY,
          payload: firstLoadedCategory.id,
        });
      }
      handleOpeningAndFetchingCategories(parseInt(categoryId))();
    }
    // eslint-disable-next-line 
  }, [categoryId, categories, dispatch]);

  const onExploreMoreBtnClick = () => {
    const nextPageNumber = pageNumber + 1;
    const categoryId =
      activeCategory === ALL_CATEGORIES_ID || !activeCategory
        ? null
        : activeCategory;

    getAllProducts(
      nextPageNumber,
      PAGE_SIZE,
      searchTerm,
      categoryId,
      currentSortOption
    )
      .then((response) => {
        const { content } = response.data;
        dispatch({
          type: ACTIONS.SET_PRODUCTS,
          payload: products.concat(content),
        });
      })
      .catch((error) => {
        console.error(error);
      });

    dispatch({ type: ACTIONS.SET_PAGE_NUMBER, payload: nextPageNumber });
  };

  const totalPages = getTotalPages(
    searchedProducts?.pageData || productsByCategories?.totalElements,
    PAGE_SIZE
  );

  const handleSortOptionChoice = async (chosenSortOption) => {
    setLoading(true);
    dispatch({ type: ACTIONS.SET_SORT_OPTION, payload: chosenSortOption });
    try {
      const response = await getAllProducts(
        pageNumber,
        PAGE_SIZE,
        undefined,
        activeCategory,
        chosenSortOption
      );
      const { content, totalElements } = response.data;
      dispatch({ type: ACTIONS.SET_PRODUCTS, payload: content });
      setProductsByCategories({ content, totalElements });
    } catch (error) {
      console.error('Error while sorting products');
    } finally {
      setLoading(false);
    }
  };

  if (initialLoading) {
    return <LoadingSpinner pageSpinner={true} />;
  }

  return (
    <div className='wrapper shop-page__wrapper'>
      <div className='container'>
        <div className='shop-page__content'>
          <CategoriesAccordion
            openedCategory={openedCategory}
            setOpenedCategory={setOpenedCategory}
            categories={categories}
            handleOpeningAndFetchingCategories={
              handleOpeningAndFetchingCategories
            }
          />
          <div className='shop-page__products'>
            <SelectField
              field={field}
              handleSortOptionChoice={handleSortOptionChoice}
            />
            {searchedProducts && searchedProducts?.content?.length === 0 ? (
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
                />
              </>
            )}
            {((searchedProducts
              ? pageNumber < searchedProducts.pageData.totalPages - 1
              : pageNumber < totalPages - 1) ||
              (productsByCategories.totalElements > PAGE_SIZE &&
                products.length < productsByCategories.totalElements)) &&
              products.length >= PAGE_SIZE && (
                <Button
                  onClick={onExploreMoreBtnClick}
                  Icon={null}
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
