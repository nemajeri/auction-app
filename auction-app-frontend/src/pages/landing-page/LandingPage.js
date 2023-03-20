import React, { useState, useEffect } from 'react';
import {
  Categories,
  HighlightedProduct,
  LandingPageProducts,
} from '../../components/landing-page/index';
import './landingPage.css';
import { useGridView } from '../../hooks/useGridView';
import { getProducts } from '../../utils/api/productsApi';
import { getCategories } from '../../utils/api/categoryApi';
import { HIGHLIGHTED_PRODUCT } from '../../utils/constants';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import Tabs from '../../components/tabs/Tabs';

const tabs = [
  { id: 'newArrivals', label: 'New Arrivals' },
  { id: 'lastChance', label: 'Last Chance' },
];

const LandingPage = () => {
  const [selectedTab, setSelectedTab] = useState(tabs[0].id);
  const GridViewProducts = useGridView(LandingPageProducts);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    fetchProductsAndCategories();
  }, []);

  async function fetchProductsAndCategories() {
    let categories = await getCategories();
    setCategories(categories.data);

    let products = await getProducts();
    setProducts(products.data);
    setLoading(false);
  }

  if (loading) {
    return <LoadingSpinner />;
  }

  const handleTabClick = (id) => {
    setSelectedTab(id);
  };

  return (
    <div className='wrapper landing-page__wrapper'>
      <div className='content'>
        <section className='landing-page__categories--and_product'>
          <Categories categories={categories} />
          {!loading && <HighlightedProduct highlightedProduct={HIGHLIGHTED_PRODUCT} />}
        </section>
        <Tabs selectedTab={selectedTab} handleTabClick={handleTabClick} tabs={tabs}/>
        <GridViewProducts products={products} />
      </div>
    </div>
  );
};

export default LandingPage;


