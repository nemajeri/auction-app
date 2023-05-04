import React from 'react';
import Form from '../../../utils/forms/Form';
import Button from '../../../utils/Button';
import {
  useStripe,
  useElements,
  CardNumberElement,
  CardExpiryElement,
  CardCvcElement,
} from '@stripe/react-stripe-js';
import { Link } from 'react-router-dom';
import { getStep3Fields } from '../../../data/multiformfields';

const ThirdStepToAddItem = ({
  prevStep,
  setProductDetails,
  handleFinalSubmit,
  sellerPath,
  errors,
  setErrors,
  initialValues
}) => {
  const fields = getStep3Fields();

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

  const stripe = useStripe();
  const elements = useElements();

  return (
    <div className='shared-form-style__wrapper'>
      <div className='shared-form-style__headline'>
        <h3>LOCATION & SHIPPING</h3>
      </div>
      <Form
        errors={errors}
        setErrors={setErrors}
        fields={fields}
        onFormStateChange={(newState) => setProductDetails(newState)}
        initialValues={initialValues}
      >
        <div className='shared-form-style__card-section'>
          <p>Featured Products</p>
          <hr />
          <div>
            <div className='third-step__credit-cards'>
              <p>We accept the following credit cards</p>
              <img src='/images/visa.png' alt='visa' />
              <img src='/images/mastercard.png' alt='mastercard' />
              <img src='/images/americanexpress.png' alt='american express' />
              <img src='/images/maestro.png' alt='maestro' />
            </div>
          </div>
          <>
            <label htmlFor='name-on-card'>Name on Card</label>
            <input type='text' name='name-on-card' placeholder='JOHN DOE' />

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
          <div className='shared-form-style__btns navigation'>
            <Link to={sellerPath}>
              <Button className={'shared-form-style__btn cancel-btn__short'}>
                Cancel
              </Button>
            </Link>
            <div className='shared-form-style__btns main-navigation'>
              <Button
                className={'shared-form-style__btn back-btn'}
                onClick={prevStep}
              >
                Back
              </Button>
              <Button
                className={'shared-form-style__btn next-btn'}
                onClick={(e) => handleFinalSubmit(e, stripe, elements)}
              >
                Done
              </Button>
            </div>
          </div>
        </div>
      </Form>
    </div>
  );
};

export default ThirdStepToAddItem;
