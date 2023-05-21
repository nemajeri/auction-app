import React, { createContext, useEffect, useReducer } from 'react';
import { getAllProducts, getSearchSuggestion } from '../utils/api/productsApi';
import { getUserByEmail } from '../utils/api/userApi';
import { PAGE_SIZE, EMPTY_STRING, OPTION_DEFAULT_SORTING } from './constants';
import jwt_decode from 'jwt-decode';
import { getJwtFromCookie } from './helperFunctions';
import { appReducer, ACTIONS } from './appReducer';
import { shopPagePath } from './paths';

export const AppContext = createContext();

const initialState = {
  searchTerm: EMPTY_STRING,
  suggestion: EMPTY_STRING,
  searchedProducts: null,
  pageNumber: 0,
  loading: false,
  activeCategory: null,
  products: [],
  user: null,
  isClearButtonPressed: false,
  isUserLoading: true,
  initialLoading: true,
  currentSortOption: OPTION_DEFAULT_SORTING,
};

export const AppContextProvider = ({ children }) => {
  const [state, dispatch] = useReducer(appReducer, initialState);

  const onSearchTermChange = (event) => {
    const searchTerm = event.target.value;
    dispatch({ type: ACTIONS.SET_SEARCH_TERM, payload: searchTerm });
  };

  const suggestAlternativePhrases = async (query) => {
    if (query.length > 2) {
      try {
        const response = await getSearchSuggestion(query);
        dispatch({ type: ACTIONS.SET_SUGGESTION, payload: response.data });
      } catch (error) {
        console.error(error);
      }
    } else {
      dispatch({ type: ACTIONS.SET_SUGGESTION, payload: EMPTY_STRING });
    }
  };

  const onSearchIconClick = async (
    event,
    categoryId,
    suggestedSearchTerm = null,
    navigate,
    pathname,
    currentSortOption
  ) => {
    event.preventDefault();
    const currentSearchTerm = suggestedSearchTerm || state.searchTerm;
    dispatch({ type: ACTIONS.SET_PAGE_NUMBER, payload: 0 });
    if (!pathname.includes(shopPagePath)) {
      navigate(shopPagePath);
    }
    try {
      const response = await getAllProducts(
        0,
        PAGE_SIZE,
        currentSearchTerm,
        categoryId,
        currentSortOption
      );
      dispatch({ type: ACTIONS.SET_LOADING, payload: true });
      dispatch({
        type: ACTIONS.SET_SEARCHED_PRODUCTS,
        payload: {
          content: response.data.content,
          pageData: response.data,
        },
      });
      dispatch({ type: ACTIONS.SET_LOADING, payload: false });
      dispatch({ type: ACTIONS.SET_SUGGESTION, payload: EMPTY_STRING });
    } catch (error) {
      console.error(error);
    }
  };

  const loadUserFromCookie = async () => {
    dispatch({ type: ACTIONS.SET_IS_USER_LOADING, payload: true });
    const jwtToken = getJwtFromCookie();
    if (jwtToken) {
      const decoded = jwt_decode(jwtToken);
      const email = decoded.sub;
      const appUser = await getUserByEmail(email);
      dispatch({ type: ACTIONS.SET_USER, payload: appUser });
    }
    dispatch({ type: ACTIONS.SET_IS_USER_LOADING, payload: false });
  };

  useEffect(() => {
    loadUserFromCookie();
  }, []);

  useEffect(() => {
    if (state.searchTerm.length <= 2) {
      dispatch({ type: ACTIONS.SET_SUGGESTION, payload: EMPTY_STRING });
    } else {
      suggestAlternativePhrases(state.searchTerm);
    }
  }, [state.searchTerm]);

  return (
    <AppContext.Provider
      value={{ dispatch, ...state, onSearchTermChange, onSearchIconClick }}
    >
      {children}
    </AppContext.Provider>
  );
};