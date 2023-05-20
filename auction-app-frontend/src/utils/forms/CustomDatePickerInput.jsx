import React from 'react';
import { FaRegCalendarAlt } from 'react-icons/fa';
import './form.css';

const CustomDatePickerInput = React.forwardRef(
  ({ value, onClick, onChange, inputPlaceholder }, ref) => {

    return (
      <div className='custom-datepicker'>
        <input
          type='text'
          readOnly
          onClick={onClick}
          value={typeof value === 'string' ? value : ''}
          onChange={onChange}
          ref={ref}
          placeholder={inputPlaceholder}
          className='custom-datepicker-input'
        />
        <FaRegCalendarAlt className='custom-datepicker-icon' />
      </div>
    );
  }
);

export default CustomDatePickerInput;
