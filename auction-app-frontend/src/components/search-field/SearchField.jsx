import React from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import './search-field.css';

const SearchField = () => {
  return (
    <div className='search'>
      <input type='text' placeholder='Try enter: Shoes' />
      <AiOutlineSearch className='search__icon' />
    </div>
  );
};

export default SearchField;
