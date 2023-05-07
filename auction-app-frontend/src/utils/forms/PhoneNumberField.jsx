import React from 'react';

const PhoneNumberField = ({
  field: { name, placeholder, type },
  onChange,
  value,
  error,
}) => {
  return (
    <div className='form-field__phone-number'>
      <input
        type={type}
        id={name}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        defaultValue={value}
      />
      <div className='form-field__phone-number--verfication_status'>
        Not verified
      </div>
      {error && (
        <div className='error-message'>
          <p>{error}</p>
        </div>
      )}
    </div>
  );
};

export default PhoneNumberField;
