import React from 'react';
import Button from '../Button';
import { AiFillFacebook } from 'react-icons/ai';
import { FcGoogle } from 'react-icons/fc';

const SocialMediaButtons = ({
  handleFacebookLogin,
  handleGoogleLogin,
  isRegister,
}) => {
  return (
    <div className='form__social-media--buttons'>
      <Button
        onClick={(e) => {
          e.preventDefault();
          handleFacebookLogin();
        }}
        className={'form__social-media--button'}
        SocialMediaIcon={AiFillFacebook}
        socialMediaClassName={'form__social-media--icon facebook-button__color'}
      >
        {isRegister ? 'Signup with Facebook' : 'Log in with Facebook'}
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
        {isRegister ? 'Signup with Gmail' : 'Log in with Gmail'}
      </Button>
    </div>
  );
};

export default SocialMediaButtons;