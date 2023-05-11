import React from 'react';

const InputField = ({
  handleFocus,
  handleBlur,
  field: { name, placeholder, type },
  onChange,
  value,
  error,
}) => {
  return (
    <div>
      <input
        type={type}
        id={name}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        onFocus={handleFocus}
        onBlur={handleBlur}
      />
      {error && (
        <div className='error-message'>
          <p>{error}</p>
        </div>
      )}
    </div>
  );
};

export default InputField;
