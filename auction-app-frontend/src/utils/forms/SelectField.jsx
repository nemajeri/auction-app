import React from 'react';
import { customStyles } from '../styles';
import Select from 'react-select';

const SelectField = ({
  field: { name, placeholder, options },
  onChange,
  value,
  error,
}) => {
  
  const handleChange = (option) => {
    onChange(name, option.value);
  };

  const selectedOption = options.find((option) => option.value === value);

  return (
    <div>
      <Select
        id={name}
        name={name}
        value={selectedOption}
        options={options}
        placeholder={placeholder}
        onChange={handleChange}
        styles={customStyles}
        components={{ IndicatorSeparator: () => null }}
      />
      {error && (
        <div className='error-message'>
          <p>{error}</p>
        </div>
      )}
    </div>
  );
};

export default SelectField;
