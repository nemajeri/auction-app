import React from 'react';


const Button = ({ onClick, children, className, Icon, iconClassName }) => {
  return (
    <button onClick={onClick} className={className}>
      <span>
        <p>{children}</p> 
        {Icon && <Icon className={iconClassName}/>}
      </span>
    </button>
  );
};


export default Button;
