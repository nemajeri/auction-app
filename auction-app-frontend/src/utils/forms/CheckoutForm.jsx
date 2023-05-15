import React from 'react';
import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';
import { PAYMENT_CURRENCY, PAYMENT_TYPE } from '../constants';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { makePayment } from '../api/userApi';
import { useNavigate } from 'react-router-dom';
import { bidsPath } from '../paths';

const CheckoutForm = ({ product }) => {
  const stripe = useStripe();
  const elements = useElements();
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!stripe || !elements) {
      return;
    }

    const cardElement = elements.getElement(CardElement);

    const { error, paymentMethod } = await stripe.createPaymentMethod({
      type: PAYMENT_TYPE,
      card: cardElement,
    });


    if (error) {
      console.log('[error]', error);
      toast.error('Payment failed. Please try again.');
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
      }
    }
  };

  return (
    <form onSubmit={handleSubmit} className='checkout-form'>
      <CardElement options={{ hidePostalCode: true }} />
      <button type='submit' disabled={!stripe}>
        Pay
      </button>
    </form>
  );
};

export default CheckoutForm;
