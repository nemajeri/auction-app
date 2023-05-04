import React, { useState, useEffect } from 'react';
import Button from '../Button';
import './form.css';
import { useLocation } from 'react-router-dom';
import { callOAuth2LoginSuccess } from '../api/authApi';
import { AiFillFacebook } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import { validateFields } from '../helperFunctions';
import { FcGoogle } from 'react-icons/fc';
import useFacebookSDK from '../../hooks/useFacebookSDK';
import useGoogleSDK from '../../hooks/useGoogleSDK';
import InputWithIcon from './InputWithIcon';
import { selectStyles, selectArrowStyles, customSelectStyles } from '../styles';

const Form = ({
  fields,
  includeSocial,
  includeRememberMe,
  onRememberMe,
  handleLoginSuccess,
  rememberMe,
  children,
  onFormStateChange,
  updateSubcategories,
  errors,
  setErrors,
  initialValues,
}) => {
  const [formState, setFormState] = useState(() => {
    const flattenedFields = fields.flatMap((field) =>
      field.fields ? field.fields : field
    );
    const initialValuesFromProps = Object.fromEntries(
      flattenedFields.map((field) => [field.name, initialValues[field.name]])
    );
    return initialValuesFromProps;
  });

  const location = useLocation();
  const navigate = useNavigate();

  const handleChange = (event) => {
    const { name, value } = event.target;
    const newState = {
      ...formState,
      [name]: value,
    };

    const field = fields.find((field) => field.name === name);
    if (field?.validation && !field.validation(value)) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        [name]: field.errorMessage,
      }));
    } else {
      setErrors((prevErrors) => ({ ...prevErrors, [name]: undefined }));
    }

    if (name === 'categoryId' && updateSubcategories) {
      updateSubcategories(value);
    }

    setFormState(newState);

    if (onFormStateChange) {
      onFormStateChange(newState);
    }
  };

  const handleGsiEvent = (response) => {
    if (response && response.credential) {
      callOAuth2LoginSuccess(
        'google',
        response.credential,
        handleLoginSuccess,
        navigate
      );
    } else {
      console.error('Google Sign-In error: credential not found');
    }
  };

  const handleGsiError = (event) => {
    console.error('Google Sign-In error:', event.detail);
  };

  const handleGoogleSDKLoad = () => {
    window.google?.accounts?.id.initialize({
      client_id: process.env.REACT_APP_GOOGLE_CLIENT_ID,
      callback: handleGsiEvent,
      onerror: handleGsiError,
    });
  };

  const handleGoogleLogin = () => {
    try {
      window.google.accounts.id.prompt();
    } catch (error) {
      console.error('Google Sign-In error:', error);
    }
  };

  const handleFacebookLogin = () => {
    window.FB.login(
      (response) => {
        if (response.authResponse) {
          const accessToken = response.authResponse.accessToken;
          callOAuth2LoginSuccess(
            'facebook',
            accessToken,
            handleLoginSuccess,
            navigate
          );
        }
      },
      { scope: 'email' }
    );
  };

  useGoogleSDK(handleGoogleSDKLoad);
  useFacebookSDK();

  const handleSubmit = async (event) => {
    event.preventDefault();

    const errors = validateFields(formState);
    setErrors(errors);

    if (Object.keys(errors).length === 0) {
      onSubmit({ ...formState, rememberMe: rememberMe });
    }

    if (onRememberMe) {
      const rememberMeCheckbox = document.querySelector('[name="rememberMe"]');
      if (rememberMeCheckbox) {
        onRememberMe(rememberMeCheckbox.checked);
      }
    }
  };

  useEffect(() => {
    setFormState((prevState) => ({
      ...prevState,
      isRememberMe: rememberMe,
    }));
  }, [rememberMe]);

  const renderField = (field) => {
    const fieldElement =
      field.type === 'select' ? (
        <div style={selectStyles}>
          <select
            id={field.name}
            name={field.name}
            value={formState[field.name]}
            onChange={handleChange}
            style={{ ...customSelectStyles }}
          >
            <option value=''>{field.placeholder}</option>
            {field.options.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
          <div style={selectArrowStyles}></div>
        </div>
      ) : field.type === 'textarea' ? (
        <textarea
          id={field.name}
          name={field.name}
          value={formState[field.name]}
          onChange={handleChange}
          placeholder={field.placeholder}
        />
      ) : field.name === 'phoneNumber' ? (
        <div className='form-field__phone-number'>
          <input
            type={field.type}
            id={field.name}
            name={field.name}
            value={formState[field.name]}
            onChange={handleChange}
            placeholder={field.placeholder}
          />
          <div className='form-field__phone-number--verfication_status'>
            Not verified
          </div>
        </div>
      ) : (
        <input
          type={field.type}
          id={field.name}
          name={field.name}
          value={formState[field.name]}
          onChange={handleChange}
          placeholder={field.placeholder}
        />
      );

    return (
      <div>
        <InputWithIcon
          icon={field.icon}
          label={field.label}
          htmlFor={field.name}
          children={fieldElement}
        />
        {errors[field.name] && (
          <div className='error-message'>
            <p>{errors[field.name]}</p>
          </div>
        )}
      </div>
    );
  };

  return (
    <form>
      {fields.map((group) =>
        group.fields ? (
          <div key={group.fields[0].name} className={group.className}>
            {group.fields.map((field) => (
              <React.Fragment key={field.name}>
                {renderField(field)}
              </React.Fragment>
            ))}
          </div>
        ) : (
          <React.Fragment key={group.name}>{renderField(group)}</React.Fragment>
        )
      )}
      {includeRememberMe && (
        <div className='form__checkbox'>
          <input
            type='checkbox'
            name='rememberMe'
            value={formState.rememberMe}
            onChange={(event) =>
              onRememberMe && onRememberMe(event.target.checked)
            }
          />
          <label htmlFor='rememberMe'>Remember me</label>
        </div>
      )}
      {children}
      {includeSocial && (
        <div className='form__social-media--buttons'>
          <Button
            onClick={(e) => {
              e.preventDefault();
              handleFacebookLogin();
            }}
            className={'form__social-media--button'}
            SocialMediaIcon={AiFillFacebook}
            socialMediaClassName={
              'form__social-media--icon facebook-button__color'
            }
          >
            {location.pathname.includes('register')
              ? 'Signup with Facebook'
              : 'Log in with Facebook'}
          </Button>
          <Button
            onClick={(e) => {
              e.preventDefault();
              handleGoogleLogin();
            }}
            className={'form__social-media--button'}
            SocialMediaIcon={FcGoogle}
            socialMediaClassName={'form__social-media--icon'}
          >
            {location.pathname.includes('register')
              ? 'Signup with Gmail'
              : 'Log in with Gmail'}
          </Button>
        </div>
      )}
    </form>
  );
};

export default Form;
