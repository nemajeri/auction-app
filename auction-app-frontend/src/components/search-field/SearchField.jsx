import React from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import './search-field.css';
import { GrFormClose } from 'react-icons/gr';

const SearchField = ({
  searchTerm,
  onSearchTermChange,
  onSearchIconClick,
  setSearchTerm,
  categoryId,
  setSearchProducts,
  setProducts,
  pathname,
  navigate,
  setIsClearButtonPressed,
  currentSortOption,
}) => {
  return (
    <div className='search'>
      <input
        type='text'
        value={searchTerm}
        placeholder='Try enter: Shoes'
        onChange={onSearchTermChange}
      />
      {searchTerm.length > 0 && (
        <GrFormClose
          className='delete__icon'
          onClick={() => {
            setSearchTerm('');
            setSearchProducts(null);
            setProducts([]);
            setIsClearButtonPressed(true);
          }}
        />
      )}
      <AiOutlineSearch
        className='search__icon'
        onClick={(event) =>
          onSearchIconClick( event, categoryId, null, navigate, pathname, currentSortOption)
        }
      />
    </div>
  );
};

export default SearchField;
