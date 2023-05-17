import React, { useState, createContext, useEffect } from 'react';
import { getAllProducts, getSearchSuggestion } from '../utils/api/productsApi';
import { getUserByEmail } from '../utils/api/userApi';
import { PAGE_SIZE, OPTION_DEFAULT_SORTING } from './constants';
import jwt_decode from 'jwt-decode';
import { getJwtFromCookie } from './helperFunctions';

export const AppContext = createContext();

export const AppContextProvider = ({ children }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [suggestion, setSuggestion] = useState('');
  const [searchedProducts, setSearchProducts] = useState(null);
  const [pageNumber, setPageNumber] = useState(0);
  const [loading, setLoading] = useState(false);
  const [activeCategory, setActiveCategory] = useState(null);
  const [products, setProducts] = useState([]);
  const [user, setUser] = useState(null);
  const [isClearButtonPressed, setIsClearButtonPressed] = useState(false);
  const [currentSortOption, setCurrentSortOption] = useState(OPTION_DEFAULT_SORTING);

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
    pathname,
    currentSortOption
  ) => {
    event.preventDefault();
    const currentSearchTerm = suggestedSearchTerm || searchTerm;
    setPageNumber(0);
    if (!pathname.includes('/shop')) {
      navigate('/shop');
    }
    getAllProducts(0, PAGE_SIZE, currentSearchTerm, categoryId, currentSortOption).then(
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

  const loadUserFromCookie = async () => {
    const jwtToken = getJwtFromCookie();
    if (jwtToken) {
      const decoded = jwt_decode(jwtToken);
      const email = decoded.sub;
      const appUser = await getUserByEmail(email);
      setUser(appUser);
    }
  };

  useEffect(() => {
    loadUserFromCookie();
  }, []);

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
        currentSortOption,
        setCurrentSortOption
      }}
    >
      {children}
    </AppContext.Provider>
  );
};
