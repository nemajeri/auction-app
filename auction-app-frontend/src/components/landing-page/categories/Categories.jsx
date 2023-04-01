import React from 'react';
import './categories.css';
import { Link } from 'react-router-dom';


const Categories = ({ categories }) => {
  return (
    <div className='categories'>
      <h3>CATEGORIES</h3>
      {categories.map((category) => (
        <div key={category.id}>
          <Link to={`/shop/${encodeURIComponent(category.categoryName).toLowerCase()}`}>
            <span>{category.categoryName}</span>
          </Link>
        </div>
      ))}
    </div>
  );
};

export default Categories;
