import React from 'react';
import './form.css';

const CustomDatePickerInput = React.forwardRef(
  ({ value, onClick, onChange, placeholder }, ref) => (
    <input
      type='text'
      onClick={onClick}
      value={value}
      onChange={onChange}
      ref={ref}
      placeholder={value ? '' : placeholder}
      className='custom-datepicker'
    />
  )
);

export default CustomDatePickerInput;
