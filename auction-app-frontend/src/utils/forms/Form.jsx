import React, { useState } from 'react';
import './form.css';
import { useLocation, useNavigate } from 'react-router-dom';
import { callOAuth2LoginSuccess } from '../api/authApi';
import useFacebookSDK from '../../hooks/useFacebookSDK';
import useGoogleSDK from '../../hooks/useGoogleSDK';
import SelectField from './SelectField';
import TextAreaField from './TextAreaField';
import PhoneNumberField from './PhoneNumberField';
import InputField from './InputField';
import StartPriceInput from './StartPriceInput';
import moment from 'moment';
import DateField from './DateField';
import SocialMediaButtons from './SocialMediaButtons';
import { flattenFields } from '../helperFunctions';

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
    const initialValuesFromProps = Object.fromEntries(
      flattenFields(fields).map((field) => [field.name, initialValues[field.name]])
    );
    return initialValuesFromProps;
  });

  const location = useLocation();
  const navigate = useNavigate();

  
  const handleValidation = (field, formState, fieldValue) => {
    if (
      field?.validation &&
      !field.validation(fieldValue, formState['startDate'])
    ) {
      setErrors((prevErrors) => ({
        ...prevErrors,
        [field.name]: field.errorMessage,
      }));
    } else {
      setErrors((prevErrors) => ({ ...prevErrors, [field.name]: undefined }));
    }
  }

  const handleChange = (eventOrName, valueFromSelect) => {
    let name, value;

    if (typeof eventOrName === 'object') {
      name = eventOrName.target.name;
      value = eventOrName.target.value;
    } else {
      name = eventOrName;
      value = valueFromSelect;
    }

    const field = flattenFields(fields).find((field) => field.name === name);

    const isDateField = field?.type === 'date';
    let fieldValue = isDateField
      ? moment.utc(value).startOf('day').toISOString()
      : value;

      let newState = {
        ...formState,
        [name]: fieldValue,
      };
      
      handleValidation(field, newState, fieldValue);
      
      if (name === 'categoryId' && updateSubcategories) {
        updateSubcategories(value);
      }
      
      setFormState(newState);
      
      if (onFormStateChange) {
        onFormStateChange(newState);
      }
  };

  const handleGsiEvent = (response) => {
    if (response?.credential) {
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
        case 'date':
          FieldComponent = DateField;
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
        <SocialMediaButtons
          handleFacebookLogin={handleFacebookLogin}
          handleGoogleLogin={handleGoogleLogin}
          isRegister={location.pathname.includes('register')}
        />
      )}
    </form>
  );
};

export default Form;