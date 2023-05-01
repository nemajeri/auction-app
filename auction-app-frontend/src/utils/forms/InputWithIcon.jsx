import React, { useState } from 'react';

const InputWithIcon = ({ icon, label, htmlFor, children }) => {
  const [isFocused, setIsFocused] = useState(false);

  const handleFocus = () => {
    setIsFocused(true);
  };

  const handleBlur = () => {
    setIsFocused(false);
  };

  return (
    <div className="input-with-icon">
      {label && <label htmlFor={htmlFor}>{label}</label>}
      {icon && (
        <div className={`icon-box${isFocused ? ' icon-box-focused' : ''}`}>
          <span className="input-icon">{icon}</span>
        </div>
      )}
      {React.cloneElement(children, { onFocus: handleFocus, onBlur: handleBlur })}
    </div>
  );
};

export default InputWithIcon;