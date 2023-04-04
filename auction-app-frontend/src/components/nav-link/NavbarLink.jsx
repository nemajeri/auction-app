import React from 'react';

const NavbarLink = ({ link: { route, label, key }, activeLink, onClick }) => {
  const handleClick = (event) => {
    event.preventDefault();
    onClick(key);
  };

  return (
    <a
      href={route}
      className={key === activeLink ? 'active-link' : ''}
      onClick={handleClick}
    >
      {label}
    </a>
  );
};

export default NavbarLink;