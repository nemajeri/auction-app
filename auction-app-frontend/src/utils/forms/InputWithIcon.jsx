import React from 'react';

const InputWithIcon = ({ icon, children }) => {
  return (
    <div className='input-with-icon'>
      {icon && <span className='input-icon'>{icon}</span>}
      {children}
    </div>
  );
};
export default InputWithIcon;
