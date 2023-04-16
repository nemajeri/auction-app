import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import Form from '../../utils/forms/Form';
import { loginUser } from '../../utils/api/authApi';
import './loginPage.css';
import { AppContext } from '../../utils/AppContextProvider';
import jwt_decode from 'jwt-decode';

const fields = [
  {
    name: 'email',
    label: 'Email',
    type: 'email',
  },
  {
    name: 'password',
    label: 'Password',
    type: 'password',
  },
];

const LoginPage = () => {
  const { setUser } = useContext(AppContext);

  const handleLoginSuccess = (jwtToken) => {
    const decoded = jwt_decode(jwtToken);
    console.log('Decoded token: ',decoded)
  };

  const handleRememberMe = () => {
    const now = new Date();
    const expirationTime = new Date(now.getTime() + 2 * 24 * 60 * 60 * 1000);
    document.cookie = `rememberMe=true; expires=${expirationTime.toUTCString()}; path=/; secure=true`;
  };

  return (
    <div className='wrapper login-page__wrapper'>
      <div className='login-page__headline'>
        <h3>LOGIN</h3>
      </div>
      <div className='login-page__content'>
        <Form
          fields={fields}
          submitText='LOGIN'
          onSubmit={(credentials) => loginUser(credentials, handleLoginSuccess)}
          includeSocial={true}
          includeRememberMe={true}
          onRememberMe={handleRememberMe}
        />
        <div className='login-page__password'>
          <Link to={'/password'}>Forgot password?</Link>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
