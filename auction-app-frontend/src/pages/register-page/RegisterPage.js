import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Form from '../../utils/forms/Form';
import { loginPath } from '../../utils/paths';
import { registerUser } from '../../utils/api/authApi';
import './registerPage.css';
import { toast } from 'react-toastify';

const fields = [
  {
    name: 'firstName',
    label: 'First Name',
    type: 'text',
    placeholder: 'John',
  },
  {
    name: 'lastName',
    label: 'Last Name',
    type: 'text',
    placeholder: 'Doe',
  },
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

const RegisterPage = () => {
  const navigate = useNavigate();

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
            registerUser(
              credentials,
              navigate,
              () =>
                toast.error(
                  'There was an error submitting the form. Please try again.'
                ),
              () =>
                toast.success(
                  'Account created succesfully! Please login with your new account.'
                )
            )
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
