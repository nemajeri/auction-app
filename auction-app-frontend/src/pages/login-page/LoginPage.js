import React, { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Form from '../../utils/forms/Form';
import { loginUser } from '../../utils/api/authApi';
import './loginPage.css';
import { AppContext } from '../../utils/AppContextProvider';
import jwt_decode from 'jwt-decode';
import { toast } from 'react-toastify';

const fields = [
  {
    name: 'email',
    label: 'Email',
    type: 'email',
    placeholder: 'user@domain.com',
  },
  {
    name: 'password',
    label: 'Password',
    type: 'password',
    placeholder: '••••••••',
  },
];

const LoginPage = () => {
  const { setUser } = useContext(AppContext);
  const [rememberMe, setRememberMe] = useState(false);

  const handleLoginSuccess = (jwtToken) => {
    const decoded = jwt_decode(jwtToken);
    const username = `${decoded.firstName} ${decoded.lastName}`;
    setUser(username);
  };

  const handleRememberMe = (isChecked) => {
    setRememberMe(isChecked);
  };

  const navigate = useNavigate();

  return (
    <div className='wrapper login-page__wrapper'>
      <div className='login-page__headline'>
        <h3>LOGIN</h3>
      </div>
      <div className='login-page__content'>
        <Form
          fields={fields}
          submitText='LOGIN'
          onSubmit={(credentials) =>
            loginUser(
              { ...credentials, rememberMe: rememberMe },
              handleLoginSuccess,
              navigate,
              () =>
                toast.error(
                  'There was an error submitting the form. Please try again.'
                ),
              () => toast.success('Login succesful!'),
              () => toast.error('Invalid email or password. Please try again.'),
              rememberMe
            )
          }
          includeSocial={true}
          includeRememberMe={true}
          onRememberMe={handleRememberMe}
          handleLoginSuccess={handleLoginSuccess}
          rememberMe={rememberMe}
        />
        <div className='login-page__password'>
          <Link to={'/password'}>Forgot password?</Link>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
