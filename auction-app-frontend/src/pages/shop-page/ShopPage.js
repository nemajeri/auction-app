import React, { useState, useEffect } from 'react';
import './shopPage.css';
import CategoriesAccordion from '../../components/shop-page/categories-accordion/CategoriesAccordion';
import ShopPageProducts from '../../components/shop-page/shop-page-products/ShopPageProducts';
import { useGridView } from '../../hooks/useGridView';
import Button from '../../utils/Button';
import { getCategories } from '../../utils/api/categoryApi';
import { getAllProdcutsByCategoryAndSearchTerm } from '../../utils/api/productsApi';
import { getSubcategories } from '../../utils/api/subcategoryApi';
import { PAGE_SIZE } from '../../utils/constants';

const ShopPage = () => {
  const GridViewProducts = useGridView(ShopPageProducts);
  const [openedCategory, setOpenedCategory] = useState({});
  const [categories, setCategories] = useState([]);
  const [subcategories, setSubcategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(9);
  const [products, setProducts] = useState([]);

  useEffect(() => {
    getCategories().then((response) => {
      setCategories(response.data);
      setLoading(false);
    });
  }, []);

  const handleOpeningAndFetchingSubcategories =
    (categoryId) => async (event) => {
      event.preventDefault();
      const category = event.target.dataset.category;
      const isOpening = !openedCategory[category];

      if (isOpening) {
        try {
          const getAllSubcategoriesResponse = await getSubcategories(
            categoryId
          );
          const getProductsByCategories =
            await getAllProdcutsByCategoryAndSearchTerm(
              pageNumber,
              pageSize,
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
    handleOpeningAndFetchingSubcategories(openedCategory)();
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
            {products.length > PAGE_SIZE && (
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
