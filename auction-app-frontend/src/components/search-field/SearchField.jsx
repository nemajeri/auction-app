import React from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import './search-field.css';
import { GrFormClose } from 'react-icons/gr'

const SearchField = ({ searchTerm, onSearchTermChange, onSearchIconClick, setSearchTerm }) => {
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
          }}
        />
      )}
      <AiOutlineSearch className='search__icon' onClick={onSearchIconClick} />
    </div>
  );
};

export default SearchField;
