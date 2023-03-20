import React from 'react';
import { BsChevronRight } from 'react-icons/bs';
import './button.css';

const Button = ({ onClick, children }) => {
  return (
    <button onClick={onClick}>
      <span>
        <h3>{children}</h3> <BsChevronRight />
      </span>
    </button>
  );
};

export default Button;
