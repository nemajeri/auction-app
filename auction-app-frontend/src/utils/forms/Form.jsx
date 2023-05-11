import React, { useState } from 'react';
import Button from '../Button';
import './form.css';
import { useLocation } from 'react-router-dom';
import { callOAuth2LoginSuccess } from '../api/authApi';
import { AiFillFacebook } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import { FcGoogle } from 'react-icons/fc';
import useFacebookSDK from '../../hooks/useFacebookSDK';
import useGoogleSDK from '../../hooks/useGoogleSDK';
import SelectField from './SelectField';
import TextAreaField from './TextAreaField';
import PhoneNumberField from './PhoneNumberField';
import InputField from './InputField';
import StartPriceInput from './StartPriceInput';

const Form = ({
  fields,
  includeSocial,
  includeRememberMe,
  onRememberMe,
  handleLoginSuccess,
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

  const handleChange = (eventOrName, valueFromSelect) => {
    let name, value;
    
    if (typeof eventOrName === 'object') {
      name = eventOrName.target.name;
      value = eventOrName.target.value;
    } else {
      name = eventOrName;
      value = valueFromSelect;
    }
  
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

  const renderField = (field) => {
    let FieldComponent;
    if (field.name === 'startPrice') {
      FieldComponent = StartPriceInput;
    } else if (field.isPhoneNumber) {
      FieldComponent = PhoneNumberField;
    } else {
      switch (field.type) {
        case 'select':
          FieldComponent = SelectField;
          break;
        case 'textarea':
          FieldComponent = TextAreaField;
          break;
        default:
          FieldComponent = InputField;
      }
    }

    return (
      <FieldComponent
        key={field.name}
        field={field}
        value={formState[field.name] || ''}
        onChange={handleChange}
        error={errors[field.name] || ''}
      />
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
