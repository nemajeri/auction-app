import React from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import './search-field.css';
import { GrFormClose } from 'react-icons/gr';
import { ACTIONS } from '../../utils/appReducer';
import { EMPTY_STRING } from '../../utils/constants';

const SearchField = ({
  categoryId,
  pathname,
  navigate,
  searchTerm,
  dispatch,
  onSearchIconClick,
  onSearchTermChange,
  sortBy,
}) => {
  return (
    <div className='search'>
      <input
        id='search'
        type='text'
        value={searchTerm}
        placeholder='Try enter: Shoes'
        onChange={onSearchTermChange}
        onKeyDown={(e) => {
          if (e.key === 'Enter') {
            onSearchIconClick(e, categoryId, null, navigate, pathname, sortBy);
          }
        }}
      />
      {searchTerm.length > 0 && (
        <GrFormClose
          className='delete__icon'
          onClick={() => {
            dispatch({ type: ACTIONS.SET_SEARCH_TERM, payload: EMPTY_STRING });
            dispatch({
              type: ACTIONS.SET_PRODUCTS,
              payload: [],
            });
            dispatch({
              type: ACTIONS.SET_CLEAR_BUTTON_PRESSED,
              payload: true,
            });
          }}
        />
      )}
      <AiOutlineSearch
        className='search__icon'
        onClick={(event) =>
          onSearchIconClick(event, categoryId, null, navigate, pathname, sortBy)
        }
      />
    </div>
  );
};

export default SearchField;
