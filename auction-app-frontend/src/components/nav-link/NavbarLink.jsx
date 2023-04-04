import React from 'react';
import { Link } from 'react-router-dom';

const NavbarLink = ({ link: { route, label, key }, activeLink, onClick }) => {
  return (
    <Link
      to={route}
      className={key === activeLink ? 'active-link' : ''}
      onClick={() => onClick(key)}
    >
      {label}
    </Link>
  );
};

export default NavbarLink;
