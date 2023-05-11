import React from 'react';
import Button from '../utils/Button';

const CallToAction = ({
  icon,
  text,
  buttonLabel,
  buttonClassName,
  onClick,
}) => {
  return (
    <div className='call-to-action'>
      {icon}
      <p>{text}</p>
      <Button onClick={onClick} className={buttonClassName}>
        {buttonLabel}
      </Button>
    </div>
  );
};

export default CallToAction;
