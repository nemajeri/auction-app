import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import { AppContext } from '../AppContextProvider';
import { landingPagePath } from '../paths';

const NotLoggedInRoute = ({ children }) => {
  const { user, isUserLoading } = useContext(AppContext);

  if (isUserLoading) {
    return null;
  }

  if (user) {
    return <Navigate to={landingPagePath} replace />;
  }

  return children;
};

export default NotLoggedInRoute;
