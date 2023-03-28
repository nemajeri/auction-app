import React from 'react';
import Product from '../product/Product';
import InfiniteScroll from 'react-infinite-scroll-component';

const LandingPageProducts = ({ products, fetchNextPage, hasMore }) => {
  return (
    <>
      <InfiniteScroll
        dataLength={products.length}
        next={fetchNextPage}
        hasMore={hasMore}
      >
        <div className="grid-view">
        {products.map((product) => (
          <Product product={product} key={product.id} />
        ))}
        </div>
      </InfiniteScroll>
    </>
  );
};

export default LandingPageProducts;
