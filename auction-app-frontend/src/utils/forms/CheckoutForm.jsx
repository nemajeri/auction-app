import React, { useRef } from 'react';
import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';
import { PAYMENT_CURRENCY, PAYMENT_TYPE } from '../constants';
import { toast } from 'react-toastify';
import { makePayment } from '../api/userApi';
import { useNavigate } from 'react-router-dom';
import { bidsPath } from '../paths';

const CheckoutForm = ({ product }) => {
  const stripe = useStripe();
  const elements = useElements();
  const navigate = useNavigate();
  const buttonRef = useRef(null);

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!stripe || !elements) {
      return;
    }

    const button = buttonRef.current;
    button.disabled = true;

    const cardElement = elements.getElement(CardElement);

    const { error, paymentMethod } = await stripe.createPaymentMethod({
      type: PAYMENT_TYPE,
      card: cardElement,
    });


    if (error) {
      console.log('[error]', error);
      toast.error('Payment failed. Please try again.');
      button.disabled = false;
    } else {
      console.log('[PaymentMethod]', paymentMethod);
      toast.info('Processing payment...');
      try {
        const response = await makePayment(
          paymentMethod.id,
          product.highestBid,
          PAYMENT_CURRENCY,
          product.id
        );

        if (response.data.status === 'succeeded') {
          toast.success('Payment successful!');

          setTimeout(() => {
            navigate(bidsPath);
          }, 5000);
        } else {
          toast.warning('Payment is pending.');
        }
      } catch (error) {
        toast.error('Payment failed. Please try again.');
        button.disabled = false; 
      }
    }
  };

  return (
    <form onSubmit={handleSubmit} className='checkout-form'>
      <CardElement options={{ hidePostalCode: true }} />
      <button type='submit' disabled={!stripe} ref={buttonRef}>
        Pay
      </button>
    </form>
  );
};

export default CheckoutForm;
