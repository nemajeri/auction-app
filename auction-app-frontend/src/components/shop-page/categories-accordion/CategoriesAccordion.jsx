import { useState } from 'react';
import './categoriesAccordion.css';

const CategoriesAccordion = () => {
  const [openedItems, setOpenedItems] = useState({});

  const handleOpening = (event) => {
    const category = event.target.dataset.category;
    setOpenedItems((prevState) => ({
      ...prevState,
      [category]: !prevState[category],
    }));
  };

  return (
    <>
      <div className='categories-accordion__wrapper'>
        <h3>PRODUCT CATEGORIES</h3>
        <div
          className='categories-accordion__category'
          data-category='fashion'
          onClick={handleOpening}
        >
          <h4>Fashion</h4>
          <p>{openedItems['fashion'] ? '-' : '+'}</p>
        </div>
        <div
          className={`categories-accordion__subcategories ${
            !openedItems['fashion'] && 'hidden'
          }`}
        >
          <div className='categories-accordion__subcategory'>
            <input type='checkbox' />
            <p>Bag&nbsp;(50)</p>
          </div>
        </div>

        <div
          className='categories-accordion__category'
          data-category='electronics'
          onClick={handleOpening}
        >
          <h4>Electronics</h4>
          <p>{openedItems['electronics'] ? '-' : '+'}</p>
        </div>
        <div
          className={`categories-accordion__subcategories ${
            !openedItems['electronics'] && 'hidden'
          }`}
        >
          <div className='categories-accordion__subcategory'>
            <input type='checkbox' />
            <p>Phone&nbsp;(20)</p>
          </div>
        </div>
      </div>
    </>
  );
};

export default CategoriesAccordion;
