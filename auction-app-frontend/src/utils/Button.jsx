import React from 'react';

const Button = ({
  onClick,
  children,
  className,
  Icon,
  iconClassName,
  SocialMediaIcon,
  socialMediaClassName,
  isOwner
}) => {
  return (
    <button onClick={onClick} className={className} disabled={isOwner}>
      <span>
        {SocialMediaIcon && <SocialMediaIcon className={socialMediaClassName}/>}
        <p>{children}</p>
        {Icon && <Icon className={iconClassName} />}
      </span>
    </button>
  );
};

export default Button;
