import React from 'react';
import Form from '../../../utils/forms/Form';
import { BsCurrencyDollar } from 'react-icons/bs';
import Button from '../../../utils/Button';
import { Link } from 'react-router-dom';

const SecondStepToAddItem = ({
  nextStep,
  prevStep,
  setStep2State,
  sellerPath,
}) => {
  const fields = [
    {
      name: 'startPrice',
      label: 'Your start Price',
      type: 'text',
      icon: <BsCurrencyDollar />,
    },
    {
      className: 'shared-form__flex',
      fields: [
        {
          name: 'startDate',
          label: 'Start date',
          type: 'date',
        },
        {
          name: 'endDate',
          label: 'End date',
          type: 'date',
        },
      ],
    },
  ];

  return (
    <div className='shared-form-style__wrapper'>
      <div className='shared-form-style__headline'>
        <h3>SET PRICES</h3>
      </div>
      <Form
        fields={fields}
        onFormStateChange={(newState) => setStep2State(newState)}
      >
        <p className='shared-form-style__hint'>
          This auction will be automatically closed when the end time comes. The
          highest bid will win the auction
        </p>
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
              onClick={nextStep}
            >
              Next
            </Button>
          </div>
        </div>
      </Form>
    </div>
  );
};

export default SecondStepToAddItem;
