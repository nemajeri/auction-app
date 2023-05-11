import React from 'react';
import { selectStyles, selectArrowStyles, customSelectStyles } from '../styles';

const SelectField = ({
  field: { name, placeholder, options },
  onChange,
  value,
  error,
}) => {
  return (
    <div style={selectStyles}>
      <select
        id={name}
        name={name}
        value={value}
        onChange={onChange}
        style={{ ...customSelectStyles }}
      >
        <option value=''>{placeholder}</option>
        {options.map((option) => (
          <option key={option.value} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
      <div style={selectArrowStyles}></div>
      {error && (
        <div className='error-message'>
          <p>{error}</p>
        </div>
      )}
    </div>
  );
};

export default SelectField;
