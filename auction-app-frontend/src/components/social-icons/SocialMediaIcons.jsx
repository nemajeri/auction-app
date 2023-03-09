import React from 'react';
import { ImFacebook, ImTwitter, ImInstagram } from 'react-icons/im';
import { TiSocialGooglePlus } from 'react-icons/ti';
import './socialMediaIcons.css'

export const SocialMeadiaIcons = () => {
  return (
    <div className='icons__list'>
      <div>
        <ImFacebook />
      </div>
      <div>
        <ImTwitter />
      </div>
      <div>
        <ImInstagram />
      </div>
      <div>
        <TiSocialGooglePlus />
      </div>
    </div>
  );
};


