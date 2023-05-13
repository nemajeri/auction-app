import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import { AppContext } from '../utils/AppContextProvider';

const ProtectedRoute = ({ children }) => {
  const { user, isUserLoading } = useContext(AppContext);

  if(isUserLoading) {
    return null;
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;

