import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import CustomDatePickerInput from './CustomDatePickerInput';
import moment from 'moment-timezone';

const DateField = ({ field: { name, placeholder }, value, onChange, ...props }) => {
  const handleChange = (date) => {
    const userTimezone = moment.tz.guess();
    const dateWithTimezone = moment.tz(date, userTimezone);
    const utcDate = dateWithTimezone.clone().tz('UTC');
    onChange(name, utcDate.toISOString());
  };

  return (
    <DatePicker
      selected={value ? new Date(value) : null}
      onChange={handleChange}
      customInput={<CustomDatePickerInput inputPlaceholder={placeholder} />}
      {...props}
    />
  );
};

export default DateField;

