import React from 'react';
import './categoriesAccordion.css';

const CategoriesAccordion = ({
  openedCategory,
  handleOpeningAndFetchingSubcategories,
  categories,
  subcategories,
}) => {
  return (
    categories && (
      <>
        <div className='categories-accordion__wrapper'>
          <h3>PRODUCT CATEGORIES</h3>
          {categories.map((category) => (
            <div key={category.id}>
              <div
                className='categories-accordion__category'
                data-category={`${category.categoryName}`}
                onClick={handleOpeningAndFetchingSubcategories(category.id)}
              >
                <h4>{category.categoryName}</h4>
                <p>{openedCategory[`${category.categoryName}`] ? '-' : '+'}</p>
              </div>
              <div
                className={`categories-accordion__subcategories ${
                  !openedCategory[`${category.categoryName}`] && 'hidden'
                }`}
              >
                {subcategories
                  .filter((subcategory) => subcategory.categoryId === category.id)
                  .map((subcategory) => {
                    return (
                      <div
                        className='categories-accordion__subcategory'
                        key={subcategory.id}
                      >
                        <input type='checkbox' />
                        <p>
                          {subcategory.subCategoryName}&nbsp;(
                          {subcategory.numberOfProducts})
                        </p>
                      </div>
                    );
                  })}
              </div>
            </div>
          ))}
        </div>
      </>
    )
  );
};

export default CategoriesAccordion;

