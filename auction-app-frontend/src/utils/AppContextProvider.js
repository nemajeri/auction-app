import React, {
  createContext,
  useMemo,
  useEffect,
  useReducer,
  useCallback,
  useRef,
} from 'react';
import { getAllProducts, getSearchSuggestion } from '../utils/api/productsApi';
import { getUserByEmail } from '../utils/api/userApi';
import { getCategories } from './api/categoryApi';
import {
  PAGE_SIZE,
  EMPTY_STRING,
  SORT_OPTIONS,
  SEARCH_TERM_VALIDATOR,
} from './constants';
import jwt_decode from 'jwt-decode';
import { getJwtFromCookie } from './helperFunctions';
import { appReducer, ACTIONS } from './appReducer';
import { shopPagePath } from './paths';

export const AppContext = createContext();

const initialState = {
  searchTerm: EMPTY_STRING,
  suggestion: EMPTY_STRING,
  totalPages: 0,
  loading: false,
  activeCategory: null,
  products: [],
  user: null,
  isUserLoading: true,
  initialLoading: true,
  isClearButtonPressed: false,
  sortBy: SORT_OPTIONS.DEFAULT_SORTING,
  categories: [],
  notifications: [],
};

export const AppContextProvider = ({ children }) => {
  const [state, dispatch] = useReducer(appReducer, initialState);
  const subscriptionRef = useRef(null);
  const webSocketServiceRef = useRef(null);
  const watchersSubscriptionRef = useRef(null);
  const watchersWebSocketServiceRef = useRef(null);

  const onSearchTermChange = useCallback((event) => {
    const searchTerm = event.target.value;
    dispatch({ type: ACTIONS.SET_SEARCH_TERM, payload: searchTerm });
  }, []);

  const suggestAlternativePhrases = async (query) => {
    if (query.trim().length > 2 && !query.match(SEARCH_TERM_VALIDATOR)) {
      try {
        const response = await getSearchSuggestion(query);
        dispatch({ type: ACTIONS.SET_SUGGESTION, payload: response.data });
      } catch (error) {
        console.error(error);
      }
    } else {
      dispatch({
        type: ACTIONS.SET_SUGGESTION,
        payload: 'No suggestion found',
      });
    }
  };

  const onSearchIconClick = useCallback(
    async (
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
    },
    [state.searchTerm]
  );

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
    (async () => {
      try {
        const response = await getCategories();
        dispatch({ type: ACTIONS.SET_CATEGORIES, payload: response.data });
      } catch (error) {
        console.error('Error fetching categories' + error);
      }
    })();
  }, []);

  useEffect(() => {
    if (state.searchTerm.length <= 2) {
      dispatch({ type: ACTIONS.SET_SUGGESTION, payload: EMPTY_STRING });
    } else {
      try {
        suggestAlternativePhrases(state.searchTerm);
      } catch (error) {
        console.error(error);
      }
    }
  }, [state.searchTerm]);

  const disconnectUser = useCallback((subscription, webSocketService) => {
    if (subscription) {
      subscription.unsubscribe();
    }
    if (webSocketService) {
      webSocketService.disconnect();
    }
    dispatch({ type: ACTIONS.SET_NOTIFICATIONS, payload: [] });
  }, []);

  const contextValue = useMemo(() => {
    return {
      dispatch,
      ...state,
      onSearchTermChange,
      onSearchIconClick,
      webSocketServiceRef,
      subscriptionRef,
      disconnectUser,
      watchersSubscriptionRef,
      watchersWebSocketServiceRef,
    };
  }, [dispatch, state, onSearchTermChange, onSearchIconClick, disconnectUser]);

  return (
    <AppContext.Provider value={contextValue}>{children}</AppContext.Provider>
  );
};
