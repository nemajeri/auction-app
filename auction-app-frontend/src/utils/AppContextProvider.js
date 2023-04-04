import React, { useState, createContext } from 'react';
import { getAllProductsBySearchTerm } from '../utils/api/productsApi'

export const AppContext = createContext();

export const AppContextProvider = ({ children }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [searchedProducts, setSearchProducts] = useState(null);

  const onSearchTermChange = (event) => {
    setSearchTerm(event.target.value);
  };

  const onSearchIconClick = (event) => {
    event.preventDefault();
    getAllProductsBySearchTerm(searchTerm).then(response => setSearchProducts(response.data))
  };

  return (
    <AppContext.Provider
      value={{
        searchTerm,
        onSearchTermChange,
        onSearchIconClick,
        searchedProducts
      }}
    >
      {children}
    </AppContext.Provider>
  );
};
