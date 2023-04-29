import React, { useState, useEffect, useContext } from 'react';
import './shopPage.css';
import CategoriesAccordion from '../../components/shop-page/categories-accordion/CategoriesAccordion';
import ShopPageProducts from '../../components/shop-page/shop-page-products/ShopPageProducts';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner.jsx';
import { useGridView } from '../../hooks/useGridView';
import Button from '../../utils/Button';
import { getCategories } from '../../utils/api/categoryApi';
import { getAllProducts } from '../../utils/api/productsApi';
import { PAGE_SIZE, ALL_CATEGORIES_ID } from '../../utils/constants';
import { AppContext } from '../../utils/AppContextProvider';
import { getTotalPages } from '../../utils/helperFunctions';
import { useParams } from 'react-router-dom';

const ShopPage = () => {
  const {
    searchTerm,
    searchedProducts,
    pageNumber,
    setPageNumber,
    loading,
    setLoading,
    activeCategory,
    setActiveCategory,
    products,
    setSearchTerm,
    setSearchProducts,
    setProducts,
    isClearButtonPressed,
    setIsClearButtonPressed,
  } = useContext(AppContext);
  const GridViewProducts = useGridView(ShopPageProducts);
  const [openedCategory, setOpenedCategory] = useState({});
  const [categories, setCategories] = useState([]);
  const [productsByCategories, setProductsByCategories] = useState({
    content: [],
    totalElements: 0,
  });
  const { categoryId } = useParams();

  useEffect(() => {
    const resetStatesOnUnmount = () => {
      setSearchTerm('');
      setProductsByCategories({ content: [], totalElements: 0 });
      setSearchProducts(null);
      setProducts([]);
    };

    return () => {
      resetStatesOnUnmount();
    };
  }, []);

  useEffect(() => {
    getCategories().then((response) => {
      setCategories(response.data);
      setLoading(false);
    });
  }, []);

  useEffect(() => {
    if (!loading && searchedProducts) {
      setProducts(searchedProducts.content);
    }

    if (isClearButtonPressed) {
      handleOpeningAndFetchingCategories(activeCategory)();
      setIsClearButtonPressed(false);
    }
  }, [loading, searchedProducts, isClearButtonPressed]);

  useEffect(() => {
    if (categoryId) {
      const firstLoadedCategory = categories.find(
        (category) => category.id === parseInt(categoryId)
      );

      if (firstLoadedCategory) {
        setOpenedCategory({ [firstLoadedCategory.categoryName]: true });
        setActiveCategory(firstLoadedCategory.id);
      }
      handleOpeningAndFetchingCategories(parseInt(categoryId))();
    }
  }, [categoryId, categories]);

  const handleOpeningAndFetchingCategories = (categoryId) => async (event) => {
    const category = event?.currentTarget.dataset.category;
    const isOpening = category ? !openedCategory[category] : true;

    if (!event && isOpening && category) {
      setOpenedCategory((prevState) => ({
        ...prevState,
        [category]: false,
      }));
      setActiveCategory(null);
      return;
    }

    try {
      setPageNumber(0);

      const productsResponse = await getAllProducts(
        0,
        PAGE_SIZE,
        searchTerm,
        categoryId === ALL_CATEGORIES_ID ? null : categoryId
      );

      const { content, totalElements } = productsResponse.data;

      if (!isOpening && category) {
        if (searchedProducts) {
          setProducts(searchedProducts.content);
        } else {
          setProducts([]);
        }
      } else {
        if (searchedProducts) {
          const filteredItems = searchedProducts.content.filter(
            (product) => product.categoryId === categoryId
          );
          setProducts(filteredItems);
        } else {
          setProducts(content);
        }
      }

      setProductsByCategories({ content, totalElements });
      setLoading(false);
    } catch (error) {
      console.error(error);
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

      setActiveCategory(isOpening ? categoryId : null);
    }
  };

  const onExploreMoreBtnClick = () => {
    const nextPageNumber = pageNumber + 1;
    const categoryId =
      activeCategory === ALL_CATEGORIES_ID || !activeCategory
        ? null
        : activeCategory;

    getAllProducts(nextPageNumber, PAGE_SIZE, searchTerm, categoryId)
      .then((response) => {
        const { content } = response.data;
        setProducts((prevProducts) => prevProducts.concat(content));
      })
      .catch((error) => {
        console.error(error);
      });

    setPageNumber(nextPageNumber);
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  const totalPages = getTotalPages(
    searchedProducts?.pageData || productsByCategories?.totalElements,
    PAGE_SIZE
  );

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
            {searchedProducts && searchedProducts?.content?.length === 0 ? (
              <div className='shop-page__no-products'>
                <h4>No products found.</h4>
                <p>Please try a different search or filter by category.</p>
              </div>
            ) : (
              <GridViewProducts
                className={'shop-page__grid-view'}
                products={products}
                currentLocation={'shop'}
              />
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
