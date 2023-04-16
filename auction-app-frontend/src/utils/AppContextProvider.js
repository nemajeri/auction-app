import React, { useState, createContext } from 'react';
import { getAllProducts, getSearchSuggestion } from '../utils/api/productsApi';
import { PAGE_SIZE } from './constants';

export const AppContext = createContext();

export const AppContextProvider = ({ children }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [suggestion, setSuggestion] = useState('');
  const [searchedProducts, setSearchProducts] = useState(null);
  const [pageNumber, setPageNumber] = useState(0);
  const [loading, setLoading] = useState(true);
  const [activeCategory, setActiveCategory] = useState(null);
  const [products, setProducts] = useState([]);
  const [user, setUser] = useState(null);

  console.log('User: ', user)

  const onSearchTermChange = (event) => {
    const searchTerm = event.target.value;
    setSearchTerm(searchTerm);
  };

  const suggestAlternativePhrases = async (query) => {
    if (query.length > 2) {
      try {
        const response = await getSearchSuggestion(query);
        setSuggestion(response.data[0]);
      } catch (error) {
        console.error(error);
      }
    } else {
      setSuggestion('');
    }
  }

  const onSearchIconClick = (event, suggestedSearchTerm = null) => {
    event.preventDefault();
    const currentSearchTerm = suggestedSearchTerm || searchTerm;
    setPageNumber(0);
    getAllProducts(0, PAGE_SIZE, currentSearchTerm, null).then((response) => {
      setLoading(true);
      setSearchProducts({
        content: response.data.content,
        pageData: response.data,
      });
      setLoading(false);
      setSuggestion('');
  
      if (response.data.content.length <= 1) {
        suggestAlternativePhrases(searchTerm);
      }
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
        setLoading,
        setSearchTerm,
        suggestion,
        setSuggestion,
        activeCategory,
        setActiveCategory,
        products,
        setProducts
        setUser
      }}
    >
      {children}
    </AppContext.Provider>
  );
};
