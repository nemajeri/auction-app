import React from 'react';
import './categories.css';


const Categories = ({ categories }) => {
  return (
    <div className='categories'>
      <h3>CATEGORIES</h3>
      {categories.map((category) => (
        <div key={category.id}>
          <span>{category.categoryName}</span>
        </div>
      ))}
    </div>
  );
};

export default Categories;
