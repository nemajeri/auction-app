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
import { getTotalPages } from '../../utils/helperFunctions';

const ShopPage = () => {
  const {
    searchTerm,
    searchedProducts,
    pageNumber,
    setPageNumber,
    setSearchProducts,
  } = useContext(AppContext);
  const GridViewProducts = useGridView(ShopPageProducts);
  const [openedCategory, setOpenedCategory] = useState({});
  const [categories, setCategories] = useState([]);
  const [subcategories, setSubcategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [products, setProducts] = useState([]);
  const [productsByCategories, setProductsByCategories] = useState({
    content: [],
    totalElements: 0,
  });
  const [currentCategoryId, setCurrentCategoryId] = useState(null);

  useEffect(() => {
    console.log('Searched products: ', searchedProducts);
    console.log('Products: ', products);
    console.log('Products by categories: ', productsByCategories);
    console.log('Current category id: ', currentCategoryId);
    console.log('Searched products page data: ', searchedProducts?.pageData);
    console.log('Products by categories  ', productsByCategories);
  }, [products, productsByCategories, searchedProducts, currentCategoryId]);

  useEffect(() => {
    getCategories().then((response) => {
      setCategories(response.data);
      setLoading(false);
    });

    if (searchedProducts !== null && pageNumber === 0) {
      setProducts(searchedProducts.content);
    }
  }, [pageNumber, searchedProducts]);

  const handleOpeningAndFetchingSubcategories =
    (categoryId) => async (event) => {
      const category = event.target.dataset.category;
      const isOpening = !openedCategory[category];

      try {
        setPageNumber(0);
        setCurrentCategoryId(categoryId);
        const subcategoriesResponse = await getSubcategories(categoryId);

        if (isOpening) {
          if (
            searchedProducts !== null &&
            searchedProducts.content.length > 0
          ) {
            const filtered = searchedProducts.content.filter(
              (product) => product.categoryId === categoryId
            );
            setProducts(filtered);
          } else {
            const productsByCategoriesResponse = await getAllProducts(
              0,
              PAGE_SIZE,
              categoryId === 10 ? null : categoryId,
              categoryId === 10 ? '' : undefined
            );

            console.log(
              'Response when clicking on category ',
              productsByCategoriesResponse
            );

            const { content, totalElements } =
              productsByCategoriesResponse.data;

            console.log('Number of elements ', totalElements);
            setProducts(content);
            setProductsByCategories({ content, totalElements });
          }
          setSubcategories(subcategoriesResponse.data);
        } else {
          setProducts(searchedProducts ? searchedProducts.content : []);
          setProductsByCategories([]);
        }
        setLoading(false);
      } catch (error) {
        console.error(error);
        setLoading(false);
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
            subcategories={subcategories}
            handleOpeningAndFetchingSubcategories={
              handleOpeningAndFetchingSubcategories
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
              />
            )}
            {((searchedProducts
              ? pageNumber < searchedProducts.pageData.totalPages - 1
              : pageNumber < totalPages - 1) ||
              (productsByCategories.totalElements > PAGE_SIZE &&
                products.length < productsByCategories.totalElements)) && (
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
