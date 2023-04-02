import React, { useContext } from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import './search-field.css';
import { AppContext } from '../../utils/AppContextProvider';

const SearchField = () => {
  const { searchTerm, onSearchTermChange } = useContext(AppContext);

  return (
    <div className='search'>
      <input
        type='text'
        value={searchTerm}
        placeholder='Try enter: Shoes'
        onChange={onSearchTermChange}
      />
      <AiOutlineSearch className='search__icon' />
    </div>
  );
};

export default SearchField;
