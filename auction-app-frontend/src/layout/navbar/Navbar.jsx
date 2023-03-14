import React, { useState } from 'react';
import { SocialMediaIcons } from '../../components/social-icons/SocialMediaIcons';
import './navbar.css';
import Logo from '../../assets/Logo';
import SearchField from '../../components/search-field/SearchField';
import NavbarLink from '../../components/nav-link/NavbarLink';
import navlinks from '../../data/navlinks.json';

const Navbar = () => {
  const [user, setUser] = useState('');
  const [activeLink, setActiveLink] = useState('');

  const onClick = (link) => {
    setActiveLink(link);
  };

  return (
    <>
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
      <div className='navbar__white'>
        <div className='navbar__container--white'>
          <Logo />
          <div className='navbar__container--search_and-links'>
            <SearchField />
            <div className='navbar__navigation'>
              {navlinks.map((link) => (
                <NavbarLink
                  link={link}
                  activeLink={activeLink}
                  onClick={onClick}
                  key={link.key}
                />
              ))}
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Navbar;
