import {
    PASSWORD_VALIDATOR,
    EMAIL_VALIDATOR,
    PASSWORD_LENGTH,
    NAME_VALIDATOR,
  } from '../utils/constants';

export const fields = [
    {
      name: 'firstName',
      label: 'First Name',
      type: 'text',
      placeholder: 'John',
      validation: (value) => NAME_VALIDATOR.test(value),
      errorMessage: 'Please enter a valid name',
    },
    {
      name: 'lastName',
      label: 'Last Name',
      type: 'text',
      placeholder: 'Doe',
      validation: (value) => NAME_VALIDATOR.test(value),
      errorMessage: 'Please enter a valid last name',
    },
    {
      name: 'email',
      label: 'Email',
      type: 'email',
      placeholder: 'user@domain.com',
      validation: (value) => EMAIL_VALIDATOR.test(value),
      errorMessage: 'Please enter a valid email address',
    },
    {
      name: 'password',
      label: 'Password',
      type: 'password',
      placeholder: '••••••••',
      validation: (value) =>
        value.length > PASSWORD_LENGTH && PASSWORD_VALIDATOR.test(value),
      errorMessage:
        'Password must be at least 8 characters and contain at least one special sign and one number',
    },
  ];
  