import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import Form from '../../utils/forms/Form';
import { loginPath } from '../../utils/paths';
import { registerUser } from '../../utils/api/authApi';
import './registerPage.css';
import { AppContext } from '../../utils/AppContextProvider';
import jwt from 'jwt-decode';

const fields = [
  {
    name: 'firstName',
    label: 'First Name',
    type: 'text',
  },
  {
    name: 'lastName',
    label: 'Last Name',
    type: 'text',
  },
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

const RegisterPage = () => {
  const { setUser } = useContext(AppContext);

  const handleRegisterSuccess = (jwtToken) => {
    const decoded = jwt.decode(jwtToken);
    setUser(decoded);
  };

  return (
    <div className='wrapper register-page__wrapper'>
      <div className='register-page__headline'>
        <h3>REGISTER</h3>
      </div>
      <div className='register-page__content'>
        <Form
          fields={fields}
          submitText='REGISTER'
          onSubmit={(credentials) =>
            registerUser(credentials, handleRegisterSuccess)
          }
          includeSocial={true}
          includeRememberMe={false}
        />
        <div className='register-page__login--option'>
          <span>Already have an account?</span>
          <Link to={loginPath}>Login</Link>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
