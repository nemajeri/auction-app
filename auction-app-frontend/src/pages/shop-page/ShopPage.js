import React, { useState, useEffect, useContext } from 'react';
import './shopPage.css';
import CategoriesAccordion from '../../components/shop-page/categories-accordion/CategoriesAccordion';
import ShopPageProducts from '../../components/shop-page/shop-page-products/ShopPageProducts';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner.jsx';
import { useGridView } from '../../hooks/useGridView';
import Button from '../../utils/Button';
import { getCategories } from '../../utils/api/categoryApi';
import { getAllProducts } from '../../utils/api/productsApi';
import { getSubcategories } from '../../utils/api/subcategoryApi';
import { PAGE_SIZE } from '../../utils/constants';
import { AppContext } from '../../utils/AppContextProvider';

const ShopPage = () => {
  const { searchTerm, searchedProducts, pageNumber, setPageNumber, setSearchProducts } =
    useContext(AppContext);
  const GridViewProducts = useGridView(ShopPageProducts);
  const [openedCategory, setOpenedCategory] = useState({});
  const [categories, setCategories] = useState([]);
  const [subcategories, setSubcategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [products, setProducts] = useState([]);
  const [productsByCategories, setProductsByCategories] = useState({});
  const [currentCategoryId, setCurrentCategoryId] = useState(null);

  useEffect(() => {
    getCategories().then((response) => {
      setCategories(response.data);
      setLoading(false);
    });

    if (searchedProducts !== null) {
      setProducts(searchedProducts.content);
    }
  }, [pageNumber, searchedProducts]);

  const handleOpeningAndFetchingSubcategories =
    (categoryId) => async (event) => {
      const category = event.target.dataset.category;
      const isOpening = !openedCategory[category];

      if (isOpening) {
        try {
          setPageNumber(0);
          setCurrentCategoryId(categoryId);
          const subcategoriesResponse = await getSubcategories(categoryId);

          const productsByCategoriesResponse = await getAllProducts(
            0,
            PAGE_SIZE,
            categoryId === 10 ? null : categoryId,
            categoryId === 10 ? '' : undefined
          );

          const { content } = productsByCategoriesResponse.data;

          setSearchProducts(null);
          setSubcategories(subcategoriesResponse.data);
          setProducts(content);
          setProductsByCategories(productsByCategoriesResponse);
          setLoading(false);
        } catch (error) {
          console.error(error);
          setLoading(false);
        }
      } else {
        setProducts([]);
        setProductsByCategories([]);
        setLoading(false);
      }

      setOpenedCategory((prevState) => {
        return {
          ...prevState,
          [category]: isOpening,
        };
      });
    };

  if (loading) {
    return <LoadingSpinner />;
  }

  const onExploreMoreBtnClick = () => {
    setPageNumber((prevPageNumber) => prevPageNumber + 1);
    const nextPageNumber = pageNumber + 1;

    if (searchedProducts) {
      getAllProducts(nextPageNumber, PAGE_SIZE, undefined, searchTerm)
        .then((response) => {
          const { content } = response.data;
          setProducts((prevProducts) => prevProducts.concat(content));
        })
        .catch((error) => {
          console.error(error);
        });
    } else {
      getAllProducts(
        nextPageNumber,
        PAGE_SIZE,
        currentCategoryId === 10 ? null : currentCategoryId
      )
        .then((response) => {
          const { content } = response.data;
          setProducts((prevProducts) => prevProducts.concat(content));
        })
        .catch((error) => {
          console.error(error);
        });
    }
  };

  const searchedProductsTotalPages = Math.ceil(
    searchedProducts?.pageData?.totalElements / PAGE_SIZE
  );
  console.log(searchedProductsTotalPages);

  const productsByCategoriesTotalPages = Math.ceil(
    productsByCategories?.data?.totalElements / PAGE_SIZE
  );
  console.log(productsByCategoriesTotalPages);

  const totalPages = searchedProducts
    ? searchedProductsTotalPages
    : productsByCategoriesTotalPages;

  return (
    <div className='wrapper shop-page__wrapper'>
      <div className='container'>
        <div className='shop-page__content'>
          <CategoriesAccordion
            openedCategory={openedCategory}
            setOpenedCategory={setOpenedCategory}
            categories={categories}
            subcategories={subcategories}
            handleOpeningAndFetchingSubcategories={
              handleOpeningAndFetchingSubcategories
            }
          />
          <div className='shop-page__products'>
            {searchedProducts && searchedProducts.length === 0 ? (
              <div className='shop-page__no-products'>
                <h4>No products found.</h4>
                <p>Please try a different search or filter by category.</p>
              </div>
            ) : (
              <GridViewProducts
                className={'shop-page__grid-view'}
                products={products}
              />
            )}
            {pageNumber < totalPages - 1 && (
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
