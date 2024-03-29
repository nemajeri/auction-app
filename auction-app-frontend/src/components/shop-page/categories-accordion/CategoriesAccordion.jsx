import React from 'react';
import './categoriesAccordion.css';

const CategoriesAccordion = ({
  openedCategoryId,
  handleOpeningAndFetchingAccordingToCategory,
  categories,
}) => {
  return (
    categories && (
        <div className='categories-accordion__wrapper'>
          <h3>PRODUCT CATEGORIES</h3>
          {categories.map((category) => (
            <div key={category.id}>
              <div
                className={`categories-accordion__category${
                    parseInt(openedCategoryId) === category.id ? ' opened' : ''
                }`}
                onClick={handleOpeningAndFetchingAccordingToCategory(category.id)}
              >
                <h4>{category.categoryName}</h4>
              </div>
            </div>
          ))}
        </div>
    )
  );
};

export default CategoriesAccordion;
