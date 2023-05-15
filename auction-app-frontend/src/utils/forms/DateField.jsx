import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import CustomDatePickerInput from './CustomDatePickerInput';

const DateField = ({ field: { name, placeholder }, value, onChange, ...props }) => {

  const handleChange = (date) => {
    onChange(name, date.toISOString());
  };

  return (
    <DatePicker
      selected={value ? new Date(value) : null}
      onChange={handleChange}
      customInput={<CustomDatePickerInput placeholder={placeholder}/>}
      {...props}
    />
  );
};

export default DateField;
