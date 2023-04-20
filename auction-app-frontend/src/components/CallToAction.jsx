import React from "react";
import  Button  from '../utils/Button';

const CallToAction = ({ icon, text, buttonLabel, buttonClassName }) => {
    return (
      <div className='call-to-action'>
        {icon}
        <p>{text}</p>
        <Button className={buttonClassName}>{buttonLabel}</Button>
      </div>
    );
  };

export default CallToAction