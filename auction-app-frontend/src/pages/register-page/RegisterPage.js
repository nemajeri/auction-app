import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Form from '../../utils/forms/Form';
import { loginPath } from '../../utils/paths';
import { registerUser } from '../../utils/api/authApi';
import './registerPage.css';
import { toast } from 'react-toastify';
import Button from '../../utils/Button';
import { validateFormFields } from '../../utils/helperFunctions';
import { fields } from '../../data/registerformfields';
import { EMPTY_STRING } from '../../utils/constants';

const RegisterPage = () => {
  const [registerUserDetails, setRegisterUserDetails] = useState({
    firstName: EMPTY_STRING,
    lastName: EMPTY_STRING,
    email: EMPTY_STRING,
    password: EMPTY_STRING,
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
            REGISTER
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
