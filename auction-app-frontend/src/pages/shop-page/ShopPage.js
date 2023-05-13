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
import SelectField from '../../utils/forms/SelectField';
import {
  FIELD_NAME,
  FIELD_PLACEHOLDER,
  OPTION_DEFAULT_SORTING,
  OPTION_START_DATE,
  OPTION_END_DATE,
  OPTION_PRICE_LOW_TO_HIGH,
  OPTION_PRICE_HIGH_TO_LOW,
} from '../../utils/constants';

const field = {
  name: FIELD_NAME,
  placeholder: FIELD_PLACEHOLDER,
  options: [
    { label: 'Default sorting', value: OPTION_DEFAULT_SORTING },
    { label: 'Sort By Newness', value: OPTION_START_DATE },
    { label: 'Sort By Time Left', value: OPTION_END_DATE },
    { label: 'Sort By Price: Low to High', value: OPTION_PRICE_LOW_TO_HIGH },
    { label: 'Sort By Price: High to Low', value: OPTION_PRICE_HIGH_TO_LOW },
  ],
};

const ShopPage = () => {
  const {
    searchTerm,
    searchedProducts,
    pageNumber,
    setPageNumber,
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
  const [currentSortOption, setCurrentSortOption] = useState('DEFAULT_SORTING');
  const [loading, setLoading] = useState(false);
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
    (async () => {
      setLoading(true);
      const response = await getCategories();
      setCategories(response.data);
      setLoading(false);
    })();
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
    if (categoryId && categories) {
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
      setLoading(true);
      setPageNumber(0);

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

      setActiveCategory(isOpening ? categoryId : null);
    }
  };

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

  const handleSortOptionChoice = async (chosenSortOption) => {
    setLoading(true);
    setCurrentSortOption(chosenSortOption);
    try {
      const response = await getAllProducts(
        pageNumber,
        PAGE_SIZE,
        undefined,
        activeCategory,
        chosenSortOption
      );
      const { content, totalElements } = response.data;

      setProducts(content);
      setProductsByCategories({ content, totalElements });
    } catch (error) {
      console.error('Error while sorting products');
    } finally {
      setLoading(false);
    }
  };

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