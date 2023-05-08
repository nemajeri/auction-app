import React from 'react';
import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';
import { API } from '../constants';

const CheckoutForm = ({ product }) => {
  const stripe = useStripe();
  const elements = useElements();

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!stripe || !elements) {
      return;
    }

    const cardElement = elements.getElement(CardElement);

    const { error, paymentMethod } = await stripe.createPaymentMethod({
      type: 'card',
      card: cardElement,
    });

    if (error) {
      console.log('[error]', error);
    } else {
      console.log('[PaymentMethod]', paymentMethod);
      try {
        const response = await API.post('/payments', {
          paymentMethodId: paymentMethod.id,
          amount: product.id,
          currency: 'usd',
        });

        console.log('Payment successful:', response.data);
      } catch (error) {
        console.log('Payment failed:', error.response.data);
      }
    }
  };

  return (
    <form onSubmit={handleSubmit} className='checkout-form'>
      <CardElement options={{ hidePostalCode: true }}/>
      <button type='submit' disabled={!stripe}>
        Pay
      </button>
    </form>
  );
};

export default CheckoutForm;
