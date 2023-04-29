import React, { useState, createContext, useEffect } from 'react';
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
  const [isClearButtonPressed, setIsClearButtonPressed] = useState(false);

  const onSearchTermChange = (event) => {
    const searchTerm = event.target.value;
    setSearchTerm(searchTerm);
  };

  const suggestAlternativePhrases = async (query) => {
    if (query.length > 2) {
      try {
        const response = await getSearchSuggestion(query);
        setSuggestion(response.data);
      } catch (error) {
        console.error(error);
      }
    } else {
      setSuggestion('');
    }
  };

  const onSearchIconClick = (
    event,
    categoryId,
    suggestedSearchTerm = null,
    navigate,
    pathname
  ) => {
    event.preventDefault();
    const currentSearchTerm = suggestedSearchTerm || searchTerm;
    setPageNumber(0);
    if (!pathname.includes('/shop')) {
      navigate('/shop');
    }
    getAllProducts(0, PAGE_SIZE, currentSearchTerm, categoryId).then(
      (response) => {
        setLoading(true);
        setSearchProducts({
          content: response.data.content,
          pageData: response.data,
        });
        setLoading(false);
        setSuggestion('');
      }
    );
  };

  useEffect(() => {
    if (searchTerm.length <= 2) {
      setSuggestion('');
    } else {
      suggestAlternativePhrases(searchTerm);
    }
  }, [searchTerm]);

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
        setProducts,
        setUser,
        user,
        isClearButtonPressed,
        setIsClearButtonPressed,
      }}
    >
      {children}
    </AppContext.Provider>
  );
};
