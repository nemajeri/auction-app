import {
  PHONE_NUMBER_VALIDATOR,
  ZIP_CODE_VALIDATOR,
  START_PRICE_VALIDATOR,
  TODAY
} from '../utils/constants';
import { countries } from './countries';

export const getStep1Fields = (categoryOptions, subcategoryOptions) => [
  {
    name: 'productName',
    label: 'What do you sell?',
    type: 'text',
    placeholder: 'eg. Targeal 7.1 Surround Sound Gaming Headset for PS4',
    validation: (value) => value !== '',
    errorMessage: 'Please enter the name of the item.',
  },
  {
    className: 'shared-form__flex',
    fields: [
      {
        name: 'categoryId',
        type: 'select',
        options: categoryOptions,
        placeholder: 'Select Category',
        validation: (value) => value !== '',
        errorMessage: 'Please select the category.',
      },
      {
        name: 'subcategoryId',
        type: 'select',
        options: subcategoryOptions,
        placeholder: 'Select Subcategory',
        validation: (value) => value !== '',
        errorMessage: 'Please select the subcategory.',
      },
    ],
  },
  {
    name: 'description',
    label: 'Description',
    type: 'textarea',
    validation: (value) => value !== '',
    errorMessage: 'Please enter some information about the item.',
  },
];

export const getStep2Fields = () => [
  {
    name: 'startPrice',
    label: 'Your start Price',
    type: 'text',
    validation: (value) => START_PRICE_VALIDATOR.test(value),
    errorMessage: 'Please enter the valid price!',
  },
  {
    className: 'shared-form__flex',
    fields: [
      {
        name: 'startDate',
        label: 'Start date',
        type: 'date',
        validation: (value) => value !== '' && new Date(value) >= TODAY,
        errorMessage: 'Please pick a valid date!',
      },
      {
        name: 'endDate',
        label: 'End date',
        type: 'date',
        validation: (value) => value !== '' && new Date(value) >= TODAY,
        errorMessage: 'Please pick a valid date!',
      },
    ],
  },
];

export const getStep3Fields = () => [
  {
    name: 'address',
    label: 'Address',
    type: 'text',
    placeholder: '123 Main Street',
    validation: (value) => value !== '',
    errorMessage: 'Please enter the right address',
  },
  {
    className: 'shared-form__flex',
    fields: [
      {
        name: 'city',
        label: 'City',
        type: 'text',
        placeholder: 'eg. Madrid',
        validation: (value) => value !== '',
        errorMessage: 'Please enter your city.',
      },
      {
        name: 'zipCode',
        label: 'Zip Code',
        type: 'text',
        placeholder: 'XXXXXXX',
        validation: (value) => ZIP_CODE_VALIDATOR.test(value),
        errorMessage: 'Please enter the valid zip code.',
      },
    ],
  },
  {
    name: 'country',
    label: 'Country',
    type: 'select',
    options: countries,
    placeholder: 'eg. Spain',
    validation: (value) => value !== '',
    errorMessage: 'Please pick the country.',
  },
  {
    name: 'phone',
    label: 'Phone Number',
    type: 'text',
    isPhoneNumber: true,
    placeholder: '+32534231564',
    validation: (value) => PHONE_NUMBER_VALIDATOR.test(value),
    errorMessage: 'Please enter the valid phone number.',
  },
];
