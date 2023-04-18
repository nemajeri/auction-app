import React, { useState, useEffect, useContext } from 'react';
import { SocialMediaIcons } from '../../components/social-icons/SocialMediaIcons';
import './navbar.css';
import Logo from '../../assets/Logo';
import SearchField from '../../components/search-field/SearchField';
import NavbarLink from '../../components/nav-link/NavbarLink';
import navlinks from '../../data/navlinks.json';
import { useLocation } from 'react-router-dom';
import { AppContext } from '../../utils/AppContextProvider';

const Navbar = () => {
  const {
    searchTerm,
    onSearchIconClick,
    setSearchTerm,
    suggestion,
    setSuggestion,
    user,
  } = useContext(AppContext);
  const [activeLink, setActiveLink] = useState('home');
  const { pathname } = useLocation();

  useEffect(() => {
    setActiveLink(pathname.replace('/', '') || 'home');
  }, [pathname]);

  return (
    <>
      <div className='navbar__black'>
        <div className='navbar__container'>
          <div>
            <SocialMediaIcons />
          </div>
          {user === null ? (
            <div className='navbar__account'>
              <Link to={loginPath}>Login</Link>
              <span>or</span>
              <Link to={registrationPath}>Create an Account</Link>
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
          {!(pathname.includes('login') || pathname.includes('register')) ? (
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
          ) : null}
        </div>
      </div>
      {suggestion !== '' && searchTerm !== '' ? (
        <div className='navbar__suggestion--pop-up'>
          <div className='navbar__suggestion--pop-up_container'>
            <span>Did you mean?</span>
            <p
              onClick={(e) => {
                if (suggestion) {
                  onSearchIconClick(e, suggestion);
                  setSearchTerm('');
                  setSuggestion('');
                } else {
                  setSuggestion('');
                }
              }}
            >
              {suggestion}
            </p>
          </div>
        </div>
      ) : null}
    </>
  );
};

export default Navbar;
