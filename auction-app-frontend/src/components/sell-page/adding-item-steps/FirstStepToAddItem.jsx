import React from 'react';
import Form from '../../../utils/forms/Form';
import Dropzone from '../../../utils/Dropzone';

const FirstStepToAddItem = ({ nextStep, setStep1State }) => {

  const fields = [
    {
      name: 'title',
      label: 'What do you sell?',
      type: 'text',
      layout: { width: '100%' },
    },
    {
      name: 'category',
      label: null,
      type: 'select',
      layout: { width: '50%' },
      options: [
        { label: 'Shoes', value: 'shoes' },
        { label: 'Home', value: 'home' },
        { label: 'Other', value: 'other' },
      ],
    },
    {
      name: 'subcategory',
      label: null,
      type: 'select',
      layout: { width: '50%' },
      options: [
        { label: 'Male', value: 'male' },
        { label: 'Female', value: 'female' },
        { label: 'Other', value: 'other' },
      ],
    },
    {
      name: 'description',
      label: 'Description',
      type: 'textfield',
      layout: { width: '100%' },
    },
  ];

  return (
    <>
      <Form fields={fields} onFormStateChange={(newState) => setStep1State(newState)}>
        <div>
          <p>100 words (700 characters)</p>
          <Dropzone />
          <button>Cancel</button>
          <button onClick={nextStep}>Next</button>
        </div>
      </Form>
    </>
  );
};

export default FirstStepToAddItem;
