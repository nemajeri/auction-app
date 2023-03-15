import React from 'react';

const NavbarLink = ({ link: { route, label, key }, activeLink, onClick }) => {
  return (
    <a
      href={route}
      className={key === activeLink ? 'active-link' : ''}
      onClick={() => onClick(key)}
    >
      {label}
    </a>
  );
};

export default NavbarLink;
