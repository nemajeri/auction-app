import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Form from '../../utils/forms/Form';
import { loginPath } from '../../utils/paths';
import { registerUser } from '../../utils/api/authApi';
import './registerPage.css';
import { toast } from 'react-toastify';
import {
  PASSWORD_VALIDATOR,
  EMAIL_VALIDATOR,
  PASSWORD_LENGTH,
  NAME_VALIDATOR,
} from '../../utils/constants';
import Button from '../../utils/Button';
import { validateFormFields } from '../../utils/helperFunctions';

const fields = [
  {
    name: 'firstName',
    label: 'First Name',
    type: 'text',
    placeholder: 'John',
    validation: (value) => NAME_VALIDATOR.test(value),
    errorMessage: 'Please enter a valid name',
  },
  {
    name: 'lastName',
    label: 'Last Name',
    type: 'text',
    placeholder: 'Doe',
    validation: (value) => NAME_VALIDATOR.test(value),
    errorMessage: 'Please enter a valid last name',
  },
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
      'Password must be at least 8 characters and contain at least one special sign and one number',
  },
];

const RegisterPage = () => {
  const [registerUserDetails, setRegisterUserDetails] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
  });

  const [errors, setErrors] = useState({});

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    let errors = validateFormFields(registerUserDetails, fields);
    setErrors(errors);

    const hasErrors = Object.values(errors).some((error) => error);

    if (!hasErrors) {
      registerUser(
        { ...registerUserDetails },
        navigate,
        () =>
          toast.error(
            'There was an error submitting the form. Please try again.'
          ),
        () =>
          toast.error(
            'There was an error submitting the form. Please try again.'
          ),
        () =>
          toast.success(
            'Account created succesfully! Please login with your new account.'
          )
      );
    }
  };

  return (
    <div className='wrapper register-page__wrapper'>
      <div className='register-page__headline'>
        <h3>REGISTER</h3>
      </div>
      <div className='register-page__content'>
        <Form
          fields={fields}
          includeSocial={true}
          includeRememberMe={false}
          setErrors={setErrors}
          onFormStateChange={(newState) => setRegisterUserDetails(newState)}
          initialValues={registerUserDetails}
          errors={errors}
        >
          <Button
            onClick={handleSubmit}
            className={'register-page__call_to-action'}
          >
            LOGIN
          </Button>
        </Form>
        <div className='register-page__login--option'>
          <span>Already have an account?</span>
          <Link to={loginPath}>Login</Link>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
