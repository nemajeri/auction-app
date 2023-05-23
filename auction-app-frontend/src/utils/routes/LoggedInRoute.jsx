import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import { AppContext } from '../AppContextProvider';
import { loginPath } from '../paths';

const LoggedInRoute = ({ children }) => {
  const { user, isUserLoading } = useContext(AppContext);

  if (isUserLoading) {
    return null;
  }

  if (!user) {
    return <Navigate to={loginPath} replace />;
  }

  return children;
};

export default LoggedInRoute;
