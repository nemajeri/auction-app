import React from 'react';

const CardDetails = () => {
  return (
    <div>
      <label>Name on Card</label>
      <input type='text' />
      <label>Card Number</label>
      <input
        type='text'
        maxLength={16}
        className='card-details__input'
        placeholder='XXXX-XXXX-XXXX-XXXX'
      />
      <label>Expiration Date</label>
      <input type='date' />
      <input type='date' />
      <label>CVC/CVV</label>
      <input type='password' maxLength={4} />
    </div>
  );
};

export default CardDetails;
