import { useEffect, useContext } from 'react';
import { AppContext } from '../utils/AppContextProvider';
import { ACTIONS } from '../utils/appReducer';

export function usePageLoading(delay = 500) {
  const { dispatch } = useContext(AppContext);

  useEffect(() => {
    dispatch({type: ACTIONS.SET_INITIAL_LOADING, payload: true});
    const timer = setTimeout(() => {
      dispatch({type: ACTIONS.SET_INITIAL_LOADING, payload: false});
    }, delay);

    return () => clearTimeout(timer);
  }, [dispatch, delay]);
}
