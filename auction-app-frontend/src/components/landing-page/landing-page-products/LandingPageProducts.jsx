import React, { useEffect, useRef } from 'react';
import Product from '../product/Product';
import InfiniteScroll from 'react-infinite-scroll-component';
import './landingPageProducts.css';
import LoadingSpinner from '../../loading-spinner/LoadingSpinner';

const LandingPageProducts = ({ products, fetchNextPage, hasMore, loading }) => {
  return (
    <>
      <div
        id='scrollableDiv'
        style={{ height: 'calc(100vh - 150px)', overflow: 'auto' }}
      >
        <InfiniteScroll
          dataLength={products.length}
          next={fetchNextPage}
          hasMore={hasMore}
          scrollableTarget='scrollableDiv'
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
