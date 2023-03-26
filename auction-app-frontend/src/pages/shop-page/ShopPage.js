import React from 'react';
import './shopPage.css';
import CategoriesAccordion from '../../components/shop-page/categories-accordion/CategoriesAccordion';
import ShopPageProducts from '../../components/shop-page/shop-page-products/ShopPageProducts';
import { useGridView } from '../../hooks/useGridView';
import Button from '../../utils/Button';

const ShopPage = () => {
  const GridViewProducts = useGridView(ShopPageProducts);
  const onExploreMoreBtnClick = () => {
    console.log('Clicked on wishlist button');
  };

  return (
    <div className='wrapper shop-page__wrapper'>
      <div className='container'>
        <div className='shop-page__content'>
          <CategoriesAccordion />
          <div className='shop-page__products'>
            <GridViewProducts className={'shop-page__grid-view'} />
            <Button
              onClick={onExploreMoreBtnClick}
              Icon={null}
              className={'shop-page__explore--more_button'}
              iconClassName={null}
            >
              Explore more
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ShopPage;
