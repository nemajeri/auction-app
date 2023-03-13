import React from 'react';
import { SocialMediaIcons } from '../social-icons/SocialMediaIcons';

const FooterItem = ({ item: { number, email, component } }) => {
  if (number) {
    return <p>{number}</p>;
  } else if (email) {
    return <p>{email}</p>;
  } else if (component === 'SocialMeadiaIcons') {
    return <SocialMediaIcons />;
  }
  return null;
};

export default FooterItem;
