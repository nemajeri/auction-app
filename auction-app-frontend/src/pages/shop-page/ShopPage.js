import React, { useState, useEffect, useContext } from 'react';
import './shopPage.css';
import CategoriesAccordion from '../../components/shop-page/categories-accordion/CategoriesAccordion';
import ShopPageProducts from '../../components/shop-page/shop-page-products/ShopPageProducts';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner.jsx';
import { useGridView } from '../../hooks/useGridView';
import Button from '../../utils/Button';
import { getCategories } from '../../utils/api/categoryApi';
import { getAllProductsByCategory } from '../../utils/api/productsApi';
import { getSubcategories } from '../../utils/api/subcategoryApi';
import { PAGE_SIZE } from '../../utils/constants';
import { AppContext } from '../../utils/AppContextProvider';

const ShopPage = () => {
  const { searchedProducts } = useContext(AppContext);
  const GridViewProducts = useGridView(ShopPageProducts);
  const [openedCategory, setOpenedCategory] = useState({});
  const [categories, setCategories] = useState([]);
  const [subcategories, setSubcategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pageNumber, setPageNumber] = useState(0);
  const [products, setProducts] = useState([]);
  const [productsByCategories, setProductsByCategories] = useState({});
  const [currentCategoryId, setCurrentCategoryId] = useState(null);

  useEffect(() => {
    getCategories().then((response) => {
      setCategories(response.data);
      setLoading(false);
    });

    if (searchedProducts !== null) {
      setProducts(searchedProducts);
    }
  }, [pageNumber, searchedProducts]);

  const handleOpeningAndFetchingSubcategories =
    (categoryId) => async (event) => {
      const category = event.target.dataset.category;
      const isOpening = !openedCategory[category];

      if (isOpening) {
        try {
          setCurrentCategoryId(categoryId);
          const subcategoriesResponse = await getSubcategories(categoryId);
          const productsByCategoriesResponse = await getAllProductsByCategory(
            0,
            PAGE_SIZE,
            categoryId
          );
          const { content } = productsByCategoriesResponse.data;
          setSubcategories(subcategoriesResponse.data);
          setProducts(content);
          setLoading(false);
          setProductsByCategories(productsByCategoriesResponse);
        } catch (error) {
          console.error(error);
          setLoading(false);
        }
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
    getAllProductsByCategory(nextPageNumber, PAGE_SIZE, currentCategoryId)
    .then((response) => {
      const { content } = response.data;
      setProducts((prevProducts) => prevProducts.concat(content));
    })
    .catch((error) => {
      console.error(error);
    });
  };

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
            <GridViewProducts
              className={'shop-page__grid-view'}
              products={products}
            />
            {pageNumber < productsByCategories?.data?.totalPages - 1 && (
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
