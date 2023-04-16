import React, { useState, useEffect } from 'react';
import Button from '../Button';
import './form.css';
import { useLocation } from 'react-router-dom';
import { callOAuth2LoginSuccess } from '../api/authApi';
import { FcGoogle } from 'react-icons/fc';
import { AiFillFacebook } from 'react-icons/ai';

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
  const googleClientId = process.env.REACT_APP_GOOGLE_CLIENT_ID;

  const handleChange = (event) => {
    setFormState({
      ...formState,
      [event.target.name]: event.target.value,
    });
  };

  useEffect(() => {
    window.gapi.load('auth2', () => {
      window.gapi.auth2.init({ client_id: googleClientId });
    });
  }, []);

  const handleGoogleLogin = () => {
    const auth2 = window.gapi.auth2.getAuthInstance();
    auth2.signIn().then((googleUser) => {
      const idToken = googleUser.getAuthResponse().id_token;
      callOAuth2LoginSuccess('google', idToken);
    });
  };

  const handleFacebookLogin = () => {
    window.FB.login(
      (response) => {
        if (response.authResponse) {
          const accessToken = response.authResponse.accessToken;
          callOAuth2LoginSuccess('facebook', accessToken);
        }
      },
      { scope: 'email', return_scopes: true }
    );
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    onSubmit(formState);

    if (onRememberMe) {
      const rememberMeCheckbox = document.querySelector('[name="rememberMe"]');
      if (rememberMeCheckbox && rememberMeCheckbox.checked) {
        onRememberMe();
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
            onChange={handleChange}
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
            onClick={handleFacebookLogin}
            className={'form__social-media--button'}
            SocialMediaIcon={AiFillFacebook}
            socialMediaClassName={'form__social-media--icon'}
          >
            {location.pathname.includes('register')
              ? 'Register with Facebook'
              : 'Log in with Facebook'}
          </Button>
          <Button
            onClick={handleGoogleLogin}
            className={'form__social-media--button'}
            SocialMediaIcon={FcGoogle}
            socialMediaClassName={'form__social-media--icon'}
          >
            {location.pathname.includes('register')
              ? 'Register with Google'
              : 'Log in with Google'}
          </Button>
        </div>
      )}
    </form>
  );
};

export default Form;
