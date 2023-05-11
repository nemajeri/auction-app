import React from 'react';

const TextAreaField = ({ field: { name, placeholder }, onChange, value, error }) => {
  return (
    <div>
      <textarea
        id={name}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
      />
      {error && (
        <div className='error-message'>
          <p>{error}</p>
        </div>
      )}
    </div>
  );
};

export default TextAreaField;
