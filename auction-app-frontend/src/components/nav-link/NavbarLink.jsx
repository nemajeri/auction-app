import React from 'react';
import { Link } from 'react-router-dom';

const NavbarLink = ({ link: { route, label, key }, activeLink, onClick }) => {
  const isActive = activeLink.includes(key);
  return (
    <Link
      to={route}
      className={`navbar__link ${isActive ? 'active-link' : ''}`}
      onClick={() => onClick(key)}
    >
      {label}
    </Link>
  );
};

export default NavbarLink;