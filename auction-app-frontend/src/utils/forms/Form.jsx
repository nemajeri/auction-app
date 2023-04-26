import React, { useState, useEffect } from 'react';
import Button from '../Button';
import './form.css';
import { useLocation } from 'react-router-dom';
import { callOAuth2LoginSuccess } from '../api/authApi';
import { AiFillFacebook } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';
import { validateFields } from '../helperFunctions';
import { FcGoogle } from 'react-icons/fc';

const Form = ({
  fields,
  submitText,
  onSubmit,
  includeSocial,
  includeRememberMe,
  onRememberMe,
  handleLoginSuccess,
}) => {
  const [formState, setFormState] = useState(
    Object.fromEntries(fields.map((field) => [field.name, '']))
  );
  const [errors, setErrors] = useState({});

  const location = useLocation();
  const navigate = useNavigate();

  const handleChange = (event) => {
    setFormState({
      ...formState,
      [event.target.name]: event.target.value,
    });
  };

  useEffect(() => {
    initFacebookSDK();

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

    window.google?.accounts?.id.initialize({
      client_id: process.env.REACT_APP_GOOGLE_CLIENT_ID,
      callback: handleGsiEvent,
      onerror: handleGsiError,
    });

    return () => {};
  }, []);

  const handleGoogleLogin = async () => {
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

  const initFacebookSDK = () => {
    window.fbAsyncInit = () => {
      window.FB.init({
        appId: process.env.REACT_APP_FACEBOOK_APP_ID,
        cookie: true,
        xfbml: true,
        version: 'v16.0',
      });
    };

    ((d, s, id) => {
      var js,
        fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) return;
      js = d.createElement(s);
      js.id = id;
      js.src = '//connect.facebook.net/en_US/sdk.js';
      fjs.parentNode.insertBefore(js, fjs);
    })(document, 'script', 'facebook-jssdk');
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    
    const errors = validateFields(formState);
    setErrors(errors);
  
    if (Object.keys(errors).length === 0) {
      onSubmit(formState);
    }
  
    if (onRememberMe) {
      const rememberMeCheckbox = document.querySelector('[name="rememberMe"]');
      if (rememberMeCheckbox) {
        onRememberMe(rememberMeCheckbox.checked);
      }
    }
  };

  return (
    <form>
      {fields.map((field) => (
        <React.Fragment key={field.name}>
          <label htmlFor={field.name}>{field.label}</label>
          <input
            type={field.type}
            id={field.name}
            name={field.name}
            value={formState[field.name]}
            onChange={handleChange}
            placeholder={field.placeholder}
            autoComplete="off"
            required
          />
          {errors[field.name] && (
            <p className='error-message'>{errors[field.name]}</p>
          )}
        </React.Fragment>
      ))}
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
      <Button onClick={handleSubmit} className={'form__main--call_to-action'}>
        {submitText}
      </Button>
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
