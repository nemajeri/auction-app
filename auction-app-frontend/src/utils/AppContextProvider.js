import React, { useState, createContext } from 'react';
import { getAllProducts } from '../utils/api/productsApi';
import { PAGE_SIZE } from './constants';

export const AppContext = createContext();

export const AppContextProvider = ({ children }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [searchedProducts, setSearchProducts] = useState(null);
  const [pageNumber, setPageNumber] = useState(0);

  const onSearchTermChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const onSearchIconClick = (event) => {
    event.preventDefault();
    setPageNumber(0);
    getAllProducts(0, PAGE_SIZE, undefined, searchTerm).then((response) => {
      setSearchProducts({
        content: response.data.content,
        pageData: response.data,
      });
    });
  };

  return (
    <AppContext.Provider
      value={{
        searchTerm,
        onSearchTermChange,
        onSearchIconClick,
        searchedProducts,
        pageNumber,
        setPageNumber
      }}
    >
      {children}
    </AppContext.Provider>
  );
};
