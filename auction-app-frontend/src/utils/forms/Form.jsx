import React, { useState, useEffect } from 'react';
import Button from '../Button';
import './form.css';
import { useLocation } from 'react-router-dom';
import { callOAuth2LoginSuccess } from '../api/authApi';
import { AiFillFacebook } from 'react-icons/ai';
import { useNavigate } from 'react-router-dom';

const Form = ({
  fields,
  submitText,
  onSubmit,
  includeSocial,
  includeRememberMe,
  onRememberMe,
}) => {
  const [formState, setFormState] = useState(
    Object.fromEntries(fields.map((field) => [field.name, '']))
  );

  const location = useLocation();
  const navigate = useNavigate();
  const googleClientId = process.env.REACT_APP_GOOGLE_CLIENT_ID;
  const facebookAppId = process.env.REACT_APP_FACEBOOK_APP_ID;

  const handleChange = (event) => {
    setFormState({
      ...formState,
      [event.target.name]: event.target.value,
    });
  };

  useEffect(() => {
    initFacebookSDK();

    const handleGsiEvent = (response) => {
      console.log('Google response:', response);
      if (response && response.credential) {
        callOAuth2LoginSuccess('google', response.credential, navigate);
      } else {
        console.error('Google Sign-In error: credential not found');
      }
    };

    const handleGsiError = (event) => {
      console.error('Google Sign-In error:', event.detail);
    };

    window.google?.accounts?.id.initialize({
      client_id: googleClientId,
      callback: handleGsiEvent,
      onerror: handleGsiError,
    });

    window.google?.accounts?.id.renderButton(
      document.querySelector('.g_id_signin'),
      {
        type: 'standard',
        shape: 'rectangular',
        theme: 'outline',
        text: 'signin_with',
        size: 'large',
        logo_alignment: 'left',
      }
    );

    return () => {};
  }, []);

  const handleFacebookLogin = () => {
    window.FB.login(
      (response) => {
        if (response.authResponse) {
          const accessToken = response.authResponse.accessToken;
          callOAuth2LoginSuccess('facebook', accessToken, navigate);
        }
      },
      { scope: 'email' }
    );
  };

  const initFacebookSDK = () => {
    window.fbAsyncInit = () => {
      window.FB.init({
        appId: facebookAppId,
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

  const handleSubmit = (event) => {
    event.preventDefault();
    onSubmit(formState);

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
          />
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
            socialMediaClassName={'form__social-media--icon'}
          >
            {location.pathname.includes('register')
              ? 'Register with Facebook'
              : 'Log in with Facebook'}
          </Button>
          <div
            id='g_id_onload'
            data-client_id={googleClientId}
            data-context='signin'
            data-ux_mode='popup'
            data-auto_prompt='false'
          ></div>
          <div
            className='g_id_signin'
            data-type='standard'
            data-shape='rectangular'
            data-theme='outline'
            data-text='signin_with'
            data-size='large'
            data-logo_alignment='left'
          ></div>
        </div>
      )}
    </form>
  );
};

export default Form;
