import React, { useState } from 'react';
import InputField from './InputField';
import { BsCurrencyDollar } from 'react-icons/bs';

const StartPriceInput = (props) => {
  const { field, value, onChange, error } = props;
  const [isFocused, setIsFocused] = useState(false);

  const handleFocus = () => {
    setIsFocused(true);
  };

  const handleBlur = () => {
    setIsFocused(false);
  };

  return (
    <div className="input-with-icon">
      {field.label && <label htmlFor={field.name}>{field.label}</label>}
      <div className={`icon-box${isFocused ? ' icon-box-focused' : ''}`}>
        <span className="input-icon">
          <BsCurrencyDollar />
        </span>
      </div>
      <InputField handleFocus={handleFocus} handleBlur={handleBlur} field={field} value={value} onChange={onChange} error={error} />
    </div>
  );
};

export default StartPriceInput;
