import React from 'react';
import {
  CardNumberElement,
  CardExpiryElement,
  CardCvcElement,
} from '@stripe/react-stripe-js';
import './form.css';

const CardInput = () => {
  const options = {
    style: {
      base: {
        fontSize: '16px',
        color: '#424770',
        '::placeholder': {
          color: '#d8d8d8',
          fontWeight: '100',
          fontSize: '1rem',
        },
        padding: '15px 20px',
        boxSizing: 'border-box',
      },
    },
  };

  return (
    <>
      <label htmlFor='name-on-card'>Name on Card</label>
      <input
        type='text'
        name='name-on-card'
        placeholder='JOHN DOE'
      />

      <label htmlFor='card-number'>Card Number</label>
      <div className='card-input__wrapper'>
        <CardNumberElement id='card-number' options={options} />
      </div>

      <div className='card-input__flex-container'>
        <div>
          <label htmlFor='expiration-date'>Expiration Date</label>
          <div className='card-input__wrapper'>
            <CardExpiryElement id='expiration-date' options={options} />
          </div>
        </div>

        <div>
          <label htmlFor='cvc'>CVC</label>
          <div className='card-input__wrapper'>
            <CardCvcElement id='cvc' options={options} />
          </div>
        </div>
      </div>

      <div id='card-errors' role='alert'></div>
    </>
  );
};

export default CardInput;