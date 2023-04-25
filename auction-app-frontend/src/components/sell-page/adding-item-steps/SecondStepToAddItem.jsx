import React from 'react';
import Form from '../../../utils/forms/Form';
import { BsCurrencyDollar } from 'react-icons/bs';

const SecondStepToAddItem = ({
  nextStep,
  prevStep,
  setStep2State,
}) => {

  const fields = [
    {
      name: 'startingPrice',
      label: 'Your start Price',
      type: 'text',
      layout: { width: '100%' },
      icon: <BsCurrencyDollar />,
    },
    {
      name: 'startDate',
      label: 'Start date',
      type: 'date',
      layout: { width: '50%' },
    },
    {
      name: 'endDate',
      label: 'End date',
      type: 'date',
      layout: { width: '50%' },
    },
  ];

  return (
    <>
      <Form fields={fields} onFormStateChange={(newState) => setStep2State(newState)}>
        <div>
          <p>
            This auction will be automatically closed when the end time comes.
            The highest bid will win the auction
          </p>
          <button>Cancel</button>
          <button onClick={prevStep}>Back</button>
          <button onClick={nextStep}>Next</button>
        </div>
      </Form>
    </>
  );
};

export default SecondStepToAddItem;
