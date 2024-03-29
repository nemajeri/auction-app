import React from 'react';
import Product from '../../../components/product/Product';
import InfiniteScroll from 'react-infinite-scroll-component';
import './landingPageProducts.css';

const LandingPageProducts = ({
  products,
  fetchNextPage,
  hasMore,
  landingPageProductClassName,
}) => {
  return (
    <>
      <div className='product__scrollable-viewport' id='products'>
        <InfiniteScroll
          dataLength={products.length}
          next={fetchNextPage}
          hasMore={hasMore}
          scrollableTarget='products'
          scrollThreshold={0.8}
        >
          <div className='grid-view'>
            {products.map((product, index) => (
              <Product
                product={product}
                key={index}
                landingPageProductClassName={landingPageProductClassName}
              />
            ))}
          </div>
        </InfiniteScroll>
      </div>
    </>
  );
};

export default LandingPageProducts;
