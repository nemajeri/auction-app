import {
    PASSWORD_VALIDATOR,
    EMAIL_VALIDATOR,
    PASSWORD_LENGTH,
  } from '../utils/constants';

export const fields = [
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
        'Password contain 8 characters, one special sign and one number',
    },
  ];