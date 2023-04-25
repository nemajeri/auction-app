import React from 'react';
import Form from '../../../utils/forms/Form';
import CardDetails from '../../../utils/forms/CardDetails';

const ThirdStepToAddItem = ({
  prevStep,
  setStep3State,
  handleFinalSubmit,
}) => {

  const fields = [
    {
      name: 'address',
      label: 'Address',
      type: 'text',
      layout: { width: '100%' },
    },
    {
      name: 'city',
      label: 'City',
      type: 'text',
      layout: { width: '50%' },
    },
    {
      name: 'zipCode',
      label: 'Zip Code',
      type: 'text',
      layout: { width: '50%' },
    },
    {
      name: 'country',
      label: 'Country',
      type: 'select',
      layout: { width: '100%' },
      options: ['Spain', 'Belgium', 'Norway'],
    },
    {
      name: 'phoneNumber',
      label: 'Phone Number',
      type: 'text',
      layout: { width: '100%' },
    },
  ];

  return (
    <>
      <Form fields={fields} onFormStateChange={(newState) => setStep3State(newState)}>
        <div>
          <p>Featured Products</p>
          <div>
            <div className='third-step__credit-cards'>
              <p>We accept the following credit cards</p>
              <img src='/images/visa.png' alt='visa' />
              <img src='/images/mastercard.png' alt='mastercard' />
              <img src='/images/americanexpress.png' alt='american express' />
              <img src='/images/maestro.png' alt='maestro' />
            </div>
          </div>
          <CardDetails />
          <button>Cancel</button>
          <button onClick={prevStep}>Back</button>
          <button onClick={handleFinalSubmit}>Done</button>
        </div>
      </Form>
    </>
  );
};

export default ThirdStepToAddItem;
