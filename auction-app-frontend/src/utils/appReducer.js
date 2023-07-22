export const ACTIONS = {
    SET_SEARCH_TERM: 'set_search_term',
    SET_SUGGESTION: 'set_suggestion',
    SET_SEARCHED_PRODUCTS: 'set_searched_products',
    SET_PAGE_NUMBER: 'set_page_number',
    SET_LOADING: 'set_loading',
    SET_ACTIVE_CATEGORY: 'set_active_category',
    SET_PRODUCTS: 'set_products',
    SET_USER: 'set_user',
    SET_CLEAR_BUTTON_PRESSED: 'set_clear_button_pressed',
    SET_SORT_OPTION: 'set_sort_option',
    SET_IS_USER_LOADING: 'set_user_is_loading',
    SET_INITIAL_LOADING: 'set_initial_loading',
  };

  export const appReducer = (state, action) => {
    switch (action.type) {
      case ACTIONS.SET_SEARCH_TERM:
        return { ...state, searchTerm: action.payload };
      case ACTIONS.SET_SUGGESTION:
        return { ...state, suggestion: action.payload };
      case ACTIONS.SET_PAGE_NUMBER:
        return { ...state, pageNumber: action.payload };
      case ACTIONS.SET_LOADING:
        return { ...state, loading: action.payload };
      case ACTIONS.SET_ACTIVE_CATEGORY:
        return { ...state, activeCategory: action.payload };
      case ACTIONS.SET_PRODUCTS:
        return { ...state, products: action.payload };
      case ACTIONS.SET_USER:
        return { ...state, user: action.payload };
      case ACTIONS.SET_CLEAR_BUTTON_PRESSED:
        return { ...state, isClearButtonPressed: action.payload };
      case ACTIONS.SET_SORT_OPTION:
        return { ...state, currentSortOption: action.payload };
      case ACTIONS.SET_IS_USER_LOADING:
        return { ...state, isUserLoading: action.payload };
      case ACTIONS.SET_INITIAL_LOADING:
        return { ...state, initialLoading: action.payload };
      default:
        return state;
    }
  }