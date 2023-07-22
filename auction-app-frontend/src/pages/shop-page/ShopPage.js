import React, { useState, useEffect, useContext, useCallback } from 'react';
import CategoriesAccordion from '../../components/shop-page/categories-accordion/CategoriesAccordion';
import ShopPageProducts from '../../components/shop-page/shop-page-products/ShopPageProducts';
import { useGridView } from '../../hooks/useGridView';
import Button from '../../utils/Button';
import { getCategories } from '../../utils/api/categoryApi';
import { getAllProducts } from '../../utils/api/productsApi';
import { AppContext } from '../../utils/AppContextProvider';
import sortingField from '../../data/options';
import { useParams } from 'react-router-dom';
import {
  SORT_OPTIONS,
  PAGE_SIZE,
  ALL_CATEGORIES_ID,
} from '../../utils/constants';
import { usePageLoading } from '../../hooks/usePageLoading';
import SelectField from '../../utils/forms/SelectField';
import axios from 'axios';

import './shopPage.css';

const ShopPage = () => {
  const { searchTerm } = useContext(AppContext);
  const GridViewProducts = useGridView(ShopPageProducts);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(false);
  const [products, setProducts] = useState([]);
  const { categoryId } = useParams();

  usePageLoading();

  const [filters, setFilters] = useState({
    sortBy: SORT_OPTIONS.DEFAULT_SORTING,
    activeCategory: null,
    pageNumber: 0,
    totalPages: 0,
  });

  const updateFilters = (key, value) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
  };

  const handleOpeningAndFetchingAccordingToCategory = useCallback(
    (categoryId) => async (event) => {
      updateFilters('activeCategory', categoryId);
      try {
        setLoading(true);
        const response = await getAllProducts(
          0,
          PAGE_SIZE,
          searchTerm,
          categoryId,
          filters.sortBy
        );
        setProducts(response.data.content);
        updateFilters('totalPages', response.data.totalPages - 1);
      } catch (error) {
        console.error(
          'Error during fetching of products according to categories'.error
        );
        throw error;
      } finally {
        setLoading(false);
      }
    },
    [searchTerm, filters.sortBy]
  );

  const onExploreMoreBtnClick = () => {
    const nextPageNumber = filters.pageNumber + 1;

    getAllProducts(
      nextPageNumber,
      PAGE_SIZE,
      searchTerm,
      filters.activeCategory
    )
      .then((response) => {
        const { content } = response.data;
        setProducts((prevState) => [...prevState, ...content]);
      })
      .catch((error) => {});

    updateFilters('pageNumber', nextPageNumber);
  };

  useEffect(() => {
    setFilters((prev) => ({ ...prev, pageNumber: 0 }));
  }, [filters.activeCategory]);

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
          return;
        } else {
          console.error('Error fetching categories' + error)
        }
      } finally {
        setLoading(false);
      }
    })();

    if (categoryId) {
      handleOpeningAndFetchingAccordingToCategory(categoryId)();
    } else if (filters.activeCategory) {
      handleOpeningAndFetchingAccordingToCategory(filters.activeCategory)();
    } else {
      handleOpeningAndFetchingAccordingToCategory(ALL_CATEGORIES_ID)();
    }

    return () => {
      source.cancel('Operation cancelled by the user.');
    };
    // eslint-disable-next-line
  }, [handleOpeningAndFetchingAccordingToCategory, categoryId]);

  return (
    <div className='wrapper shop-page__wrapper'>
      <div className='container'>
        <div className='shop-page__content'>
          <CategoriesAccordion
            openedCategoryId={filters.activeCategory}
            categories={categories}
            handleOpeningAndFetchingAccordingToCategory={
              handleOpeningAndFetchingAccordingToCategory
            }
          />
          <div className='shop-page__products'>
            <SelectField
              field={sortingField}
              handleSortOptionChoice={updateFilters}
            />
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
            {filters.pageNumber < filters.totalPages && (
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
