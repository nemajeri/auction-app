import React, { useContext } from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import './search-field.css';
import { GrFormClose } from 'react-icons/gr';
import { ACTIONS } from '../../utils/appReducer';
import { AppContext } from '../../utils/AppContextProvider';
import { EMPTY_STRING } from '../../utils/constants';

const SearchField = ({ categoryId, pathname, navigate }) => {
  const { dispatch, searchTerm, onSearchIconClick, onSearchTermChange, currentSortOption } =
    useContext(AppContext);

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
            dispatch({ type: ACTIONS.SET_SEARCH_TERM, payload: EMPTY_STRING });
            dispatch({
              type: ACTIONS.SET_SEARCHED_PRODUCTS,
              payload: null,
            });
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
          onSearchIconClick( event, categoryId, null, navigate, pathname, currentSortOption)
        }
      />
    </div>
  );
};

export default SearchField;
