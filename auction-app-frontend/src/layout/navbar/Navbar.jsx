import React, { useState, useEffect } from 'react';
import { SocialMediaIcons } from '../../components/social-icons/SocialMediaIcons';
import './navbar.css';
import Logo from '../../assets/Logo';
import SearchField from '../../components/search-field/SearchField';
import NavbarLink from '../../components/nav-link/NavbarLink';
import navlinks from '../../data/navlinks.json';
import { useLocation } from 'react-router-dom';

const Navbar = () => {
  const [user, setUser] = useState('');
  const [activeLink, setActiveLink] = useState('home');
  const { pathname } = useLocation();

  useEffect(() => {
    setActiveLink(pathname.replace('/', '') || 'home');
  }, [pathname]);

  return (
    <>
      <div className='navbar__black' id='navbar'>
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
                  key={link.key}
                  activeLink={activeLink}
                  onClick={setActiveLink}
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
