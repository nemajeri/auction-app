import React from 'react';
import Form from '../../../utils/forms/Form';
import Dropzone from '../../../utils/Dropzone';
import Button from '../../../utils/Button';

const FirstStepToAddItem = ({ nextStep, setStep1State }) => {
  const fields = [
    {
      name: 'title',
      label: 'What do you sell?',
      type: 'text',
      placeholder: 'eg. Targeal 7.1 Surround Sound Gaming Headset for PS4',
    },
    {
      className: 'shared-form__flex',
      fields: [
        {
          name: 'category',
          type: 'select',
          options: [
            { label: 'Shoes', value: 'shoes' },
            { label: 'Home', value: 'home' },
            { label: 'Other', value: 'other' },
          ],
          placeholder: 'Select Category',
        },
        {
          name: 'subcategory',
          type: 'select',
          options: [
            { label: 'Male', value: 'male' },
            { label: 'Female', value: 'female' },
            { label: 'Other', value: 'other' },
          ],
          placeholder: 'Select Subcategory',
        },
      ],
    },
    {
      name: 'description',
      label: 'Description',
      type: 'textarea',
    },
  ];

  return (
    <div className='shared-form-style__wrapper'>
      <div className='shared-form-style__headline'>
        <h3>ADD ITEM</h3>
      </div>
      <Form
        fields={fields}
        onFormStateChange={(newState) => setStep1State(newState)}
      >
        <div className='shared-form-style__align--text_right'>
          <p>100 words (700 characters)</p>
          <Dropzone />
          <div className='shared-form-style__btns'>
            <Button className={'shared-form-style__btn cancel-btn'}>
              Cancel
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

export default FirstStepToAddItem;
