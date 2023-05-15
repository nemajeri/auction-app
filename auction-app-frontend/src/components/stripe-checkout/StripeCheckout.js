import React from 'react';
import { loadStripe } from '@stripe/stripe-js';
import { Elements } from '@stripe/react-stripe-js';

import CheckoutForm from '../../utils/forms/CheckoutForm';

const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLISHED_KEY);

const StripeCheckout = ({ product }) => {
  return (
    <Elements stripe={stripePromise}>
      <CheckoutForm product={product} />
    </Elements>
  );
};

export default StripeCheckout;
