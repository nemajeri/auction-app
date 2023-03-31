import React from 'react';
import { BsChevronRight } from 'react-icons/bs';

const Button = ({ onClick, children, className }) => {
  return (
    <button onClick={onClick} className={className}>
      <span>
        <h3>{children}</h3> <BsChevronRight />
      </span>
    </button>
  );
};

export default Button;
