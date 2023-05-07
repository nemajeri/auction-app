import React, { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Form from '../../utils/forms/Form';
import { loginUser } from '../../utils/api/authApi';
import { getUserByEmail } from '../../utils/api/userApi';
import './loginPage.css';
import { AppContext } from '../../utils/AppContextProvider';
import jwt_decode from 'jwt-decode';
import { toast } from 'react-toastify';
import Button from '../../utils/Button';
import {
  PASSWORD_VALIDATOR,
  EMAIL_VALIDATOR,
  PASSWORD_LENGTH,
} from '../../utils/constants';
import { validateFormFields } from '../../utils/helperFunctions';

const fields = [
  {
    name: 'email',
    label: 'Email',
    type: 'email',
    placeholder: 'user@domain.com',
    validation: (value) => EMAIL_VALIDATOR.test(value),
    errorMessage: 'Please enter a valid email address',
  },
  {
    name: 'password',
    label: 'Password',
    type: 'password',
    placeholder: '••••••••',
    validation: (value) =>
      value.length > PASSWORD_LENGTH && PASSWORD_VALIDATOR.test(value),
    errorMessage:
      'Password must be at least 8 characters, contain at least one special sign and one number',
  },
];

const LoginPage = () => {
  const { setUser } = useContext(AppContext);
  const [loginUserDetails, setLoginUserDetails] = useState({
    email: '',
    password: '',
    rememberMe: false,
  });

  const [errors, setErrors] = useState({});

  const handleLoginSuccess = async (jwtToken) => {
    const decoded = jwt_decode(jwtToken);
    const email = decoded.sub;
    const appUser = await getUserByEmail(email);
    setUser(appUser);
  };

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    let errors = validateFormFields(loginUserDetails, fields);
    setErrors(errors);

    const hasErrors = Object.values(errors).some((error) => error);
    if (!hasErrors) {
      loginUser(
        { ...loginUserDetails },
        handleLoginSuccess,
        navigate,
        () =>
          toast.error(
            'There was an error submitting the form. Please try again.'
          ),
        () => toast.success('Login successful!'),
        () => toast.error('Invalid email or password. Please try again.')
      );
    }
  };

  return (
    <div className='wrapper login-page__wrapper'>
      <div className='login-page__headline'>
        <h3>LOGIN</h3>
      </div>
      <div className='login-page__content'>
        <Form
          fields={fields}
          includeSocial={true}
          includeRememberMe={true}
          onRememberMe={(isChecked) => {
            setLoginUserDetails((prevState) => ({
              ...prevState,
              rememberMe: isChecked,
            }));
          }}
          handleLoginSuccess={handleLoginSuccess}
          initialValues={loginUserDetails}
          errors={errors}
          setErrors={setErrors}
          onFormStateChange={(newState) => setLoginUserDetails(newState)}
        >
          <Button
            onClick={handleSubmit}
            className={'login-page__call_to-action'}
          >
            LOGIN
          </Button>
        </Form>
        <div className='login-page__password'>
          <Link to={'/password'}>Forgot password?</Link>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
