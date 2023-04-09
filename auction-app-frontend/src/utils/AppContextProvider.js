import React, { useState, createContext } from 'react';
import { getAllProducts } from '../utils/api/productsApi';
import { PAGE_SIZE } from './constants';

export const AppContext = createContext();

export const AppContextProvider = ({ children }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [searchedProducts, setSearchProducts] = useState(null);
  const [pageNumber, setPageNumber] = useState(0);
  const [loading, setLoading] = useState(true);

  const onSearchTermChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const onSearchIconClick = (event) => {
    event.preventDefault();
    setPageNumber(0);
    getAllProducts(0, PAGE_SIZE, searchTerm, null).then((response) => {
      setLoading(true);
      setSearchProducts({
        content: response.data.content,
        pageData: response.data,
      });
      setLoading(false);
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
        setPageNumber,
        setSearchProducts,
        loading,
        setLoading
      }}
    >
      {children}
    </AppContext.Provider>
  );
};
