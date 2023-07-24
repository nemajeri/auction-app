import React from 'react';
import { customStyles } from '../styles';
import Select from 'react-select';
import { ACTIONS } from '../appReducer';

const SelectField = ({ field: { name, placeholder, options }, dispatch }) => {
  const onChange = (option) =>
    dispatch({ type: ACTIONS.SET_SORT_BY, payload: option.value });

  return (
    <div>
      <Select
        id={name}
        name={name}
        options={options}
        placeholder={placeholder}
        onChange={onChange}
        styles={customStyles}
        isSearchable={false}
        components={{ IndicatorSeparator: () => null }}
      />
    </div>
  );
};

export default SelectField;
