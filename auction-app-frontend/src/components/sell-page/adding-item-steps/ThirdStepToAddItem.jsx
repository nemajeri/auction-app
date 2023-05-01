import React from 'react';
import Form from '../../../utils/forms/Form';
import CardInput from '../../../utils/forms/CardInput';
import Button from '../../../utils/Button';

const ThirdStepToAddItem = ({ prevStep, setStep3State, handleFinalSubmit }) => {
  const fields = [
    {
      name: 'address',
      label: 'Address',
      type: 'text',
      placeholder: '123 Main Street',
    },
    {
      className: 'shared-form__flex',
      fields: [
        {
          name: 'city',
          label: 'City',
          type: 'text',
          placeholder: 'eg. Madrid',
        },
        {
          name: 'zipCode',
          label: 'Zip Code',
          type: 'text',
          placeholder: 'XXXXXXX',
        },
      ],
    },
    {
      name: 'country',
      label: 'Country',
      type: 'select',
      options: [
        { label: 'Italy', value: 'italy' },
        { label: 'France', value: 'france' },
        { label: 'Germany', value: 'germany' },
      ],
      placeholder: 'eg. Spain',
    },
    {
      name: 'phoneNumber',
      label: 'Phone Number',
      type: 'text',
      placeholder: '+32534231564',
    },
  ];

  return (
    <div className='shared-form-style__wrapper'>
      <div className='shared-form-style__headline'>
        <h3>LOCATION & SHIPPING</h3>
      </div>
      <Form
        fields={fields}
        onFormStateChange={(newState) => setStep3State(newState)}
      >
        <div className='shared-form-style__card-section'>
          <p>Featured Products</p>
          <hr/>
          <div>
            <div className='third-step__credit-cards'>
              <p>We accept the following credit cards</p>
              <img src='/images/visa.png' alt='visa' />
              <img src='/images/mastercard.png' alt='mastercard' />
              <img src='/images/americanexpress.png' alt='american express' />
              <img src='/images/maestro.png' alt='maestro' />
            </div>
          </div>
          <CardInput />
          <div className='shared-form-style__btns navigation'>
          <Button className={'shared-form-style__btn cancel-btn'}>
            Cancel
          </Button>
          <div className='shared-form-style__btns main-navigation'>
            <Button
              className={'shared-form-style__btn back-btn'}
              onClick={prevStep}
            >
              Back
            </Button>
            <Button
              className={'shared-form-style__btn next-btn'}
              onClick={handleFinalSubmit}
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
