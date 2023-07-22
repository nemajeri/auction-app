import React from 'react';
import { customStyles } from '../styles';
import Select from 'react-select';

const SelectField = ({
  field: { name, placeholder, options },
  handleSortOptionChoice = () => {},
}) => {
  return (
    <div>
      <Select
        id={name}
        name={name}
        options={options}
        placeholder={placeholder}
        onChange={(option) => handleSortOptionChoice('sortBy', option.value)}
        styles={customStyles}
        isSearchable={false}
        components={{ IndicatorSeparator: () => null }}
      />
    </div>
  );
};

export default SelectField;
