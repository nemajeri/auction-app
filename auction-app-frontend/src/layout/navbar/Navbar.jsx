import React, { useState } from 'react';
import { SocialMediaIcons } from '../../components/social-icons/SocialMediaIcons';
import './navbar.css';

const Navbar = () => {
  const [user, setUser] = useState('');

  return (
    <div className='navbar__black'>
      <div className='navbar__container'>
        <div>
          <SocialMediaIcons />
        </div>
        {user === '' ? (
          <div className='navbar__account'>
            <a href='/temp/route'>Login</a>
            <span>or</span>
            <a href='/temp/route'>Create an Account</a>
          </div>
        ) : (
          <div>
            <p>Hi, {user}</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Navbar;
