import { useEffect, useContext } from 'react';
import { AppContext } from '../utils/AppContextProvider';

export function usePageLoading(delay = 500) {
  const { setInitialLoading } = useContext(AppContext);

  useEffect(() => {
    setInitialLoading(true);
    const timer = setTimeout(() => {
      setInitialLoading(false);
    }, delay);

    return () => clearTimeout(timer);
  }, [setInitialLoading, delay]);
}
