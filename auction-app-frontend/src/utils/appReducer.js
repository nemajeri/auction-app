export const ACTIONS = {
  SET_SEARCH_TERM: 'set_search_term',
  SET_SUGGESTION: 'set_suggestion',
  SET_LOADING: 'set_loading',
  SET_ACTIVE_CATEGORY: 'set_active_category',
  SET_PRODUCTS: 'set_products',
  SET_USER: 'set_user',
  SET_SORT_BY: 'set_sort_by',
  SET_CLEAR_BUTTON_PRESSED: 'set_clear_button_pressed',
  SET_IS_USER_LOADING: 'set_user_is_loading',
  SET_INITIAL_LOADING: 'set_initial_loading',
  SET_TOTAL_PAGES: 'set_total_pages',
  SET_INITIAL_PRODUCTS: 'set_initial_products',
  SET_CATEGORIES: 'set_categories',
  SET_NOTIFICATIONS: 'set_notifications',
};

export const appReducer = (state, action) => {
  switch (action.type) {
    case ACTIONS.SET_SEARCH_TERM:
      return { ...state, searchTerm: action.payload };
    case ACTIONS.SET_SUGGESTION:
      return { ...state, suggestion: action.payload };
    case ACTIONS.SET_LOADING:
      return { ...state, loading: action.payload };
    case ACTIONS.SET_ACTIVE_CATEGORY:
      return { ...state, activeCategory: action.payload };
    case ACTIONS.SET_INITIAL_PRODUCTS:
      return { ...state, products: action.payload };
    case ACTIONS.SET_PRODUCTS:
      return { ...state, products: [...state.products, ...action.payload] };
    case ACTIONS.SET_USER:
      return { ...state, user: action.payload };
    case ACTIONS.SET_CLEAR_BUTTON_PRESSED:
      return { ...state, isClearButtonPressed: action.payload };
    case ACTIONS.SET_SORT_BY:
      return { ...state, sortBy: action.payload };
    case ACTIONS.SET_TOTAL_PAGES:
      return { ...state, totalPages: action.payload };
    case ACTIONS.SET_IS_USER_LOADING:
      return { ...state, isUserLoading: action.payload };
      case ACTIONS.SET_CATEGORIES:
        return { ...state, categories: action.payload };
    case ACTIONS.SET_INITIAL_LOADING:
      return { ...state, initialLoading: action.payload };
    case ACTIONS.SET_NOTIFICATIONS:
      return {...state, notifications: [...state.notifications, action.payload]}  
    default:
      return state;
  }
};
