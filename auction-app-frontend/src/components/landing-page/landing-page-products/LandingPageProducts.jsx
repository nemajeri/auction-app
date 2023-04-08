import React from 'react';
import Product from '../../../components/product/Product';
import InfiniteScroll from 'react-infinite-scroll-component';
import './landingPageProducts.css';
import LoadingSpinner from '../../loading-spinner/LoadingSpinner';

const LandingPageProducts = ({ products, fetchNextPage, hasMore, loading }) => {
  return (
    <>
      <div
        className='product__scrollable-viewport'
      >
        <InfiniteScroll
          dataLength={products.length}
          next={fetchNextPage}
          hasMore={hasMore}
          scrollableTarget='product__scrollable-viewport'
          scrollThreshold={0.8}
        >
          {loading && <LoadingSpinner />}
          <div className='grid-view'>
            {products.map((product) => (
              <Product product={product} key={product.id} />
            ))}
          </div>
        </InfiniteScroll>
      </div>
    </>
  );
};

export default LandingPageProducts;
