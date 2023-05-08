import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import { AppContext } from '../utils/AppContextProvider';

const ProtectedRoute = ({ children }) => {
  const { user } = useContext(AppContext);

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;

