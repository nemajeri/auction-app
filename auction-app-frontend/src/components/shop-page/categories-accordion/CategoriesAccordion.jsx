import React from 'react';
import './categoriesAccordion.css';

const CategoriesAccordion = ({
  openedCategory,
  handleOpeningAndFetchingSubcategories,
  categories,
}) => {
  return (
    categories && (
      <>
        <div className='categories-accordion__wrapper'>
          <h3>PRODUCT CATEGORIES</h3>
          {categories.map((category) => (
            <div key={category.id}>
              <div
                className={`categories-accordion__category${
                  openedCategory[`${category.categoryName}`] ? ' opened' : ''
                }`}
                data-category={`${category.categoryName}`}
                onClick={handleOpeningAndFetchingSubcategories(category.id)}
              >
                <h4>{category.categoryName}</h4>
                <p>{openedCategory[`${category.categoryName}`]}</p>
              </div>
            </div>
          ))}
        </div>
      </>
    )
  );
};

export default CategoriesAccordion;
