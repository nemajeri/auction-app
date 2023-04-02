import React, { useState, useEffect, useContext } from 'react';
import './shopPage.css';
import CategoriesAccordion from '../../components/shop-page/categories-accordion/CategoriesAccordion';
import ShopPageProducts from '../../components/shop-page/shop-page-products/ShopPageProducts';
import { useGridView } from '../../hooks/useGridView';
import Button from '../../utils/Button';
import { getCategories } from '../../utils/api/categoryApi';
import {
  getAllProductsByCategory,
  getProductAsItems,
} from '../../utils/api/productsApi';
import { getSubcategories } from '../../utils/api/subcategoryApi';
import { PAGE_SIZE } from '../../utils/constants';
import { AppContext } from '../../utils/AppContextProvider';

const ShopPage = () => {
  const { searchTerm } = useContext(AppContext);
  const GridViewProducts = useGridView(ShopPageProducts);
  const [openedCategory, setOpenedCategory] = useState({});
  const [categories, setCategories] = useState([]);
  const [subcategories, setSubcategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);
  const [pageNumber, setPageNumber] = useState(0);
  const [products, setProducts] = useState([]);

  useEffect(() => {
    getCategories().then((response) => {
      setCategories(response.data);
      setLoading(false);
    });

    getProductAsItems(pageNumber, PAGE_SIZE).then((response) => {
      if (pageNumber === 0) {
        setProducts(response.data.content);
      } else {
        setProducts([...products, ...response.data.content]);
      }
    });
  }, [pageNumber]);

  const handleOpeningAndFetchingSubcategories =
    (categoryId) => async (event) => {
      const category = event.target.dataset.category;
      const isOpening = !openedCategory[category];

      if (isOpening) {
        try {
          const getAllSubcategoriesResponse = await getSubcategories(
            categoryId
          );
          const getProductsByCategories = await getAllProductsByCategory(
            pageNumber,
            PAGE_SIZE,
            categoryId
          );
          setSubcategories(getAllSubcategoriesResponse.data);

          if (pageNumber === 0) {
            setProducts(getProductsByCategories.data.content);
          } else {
            setProducts([...products, ...getProductsByCategories.data.content]);
          }
          setLoading(false);
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

  const onExploreMoreBtnClick = () => {
    setPageNumber((prevPageNumber) => prevPageNumber + 1);
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
            {products && products.length >= PAGE_SIZE && (
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
