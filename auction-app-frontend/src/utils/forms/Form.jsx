import React, { useState } from 'react';
import Button from '../Button';
import './form.css';
import { useLocation, useNavigate } from 'react-router-dom';
import { getJwtFromCookie } from '../helperFunctions';
import jwt from 'jwt-decode';

const Form = ({
  fields,
  submitText,
  onSubmit,
  includeSocial,
  includeRememberMe,
}) => {
  const [formState, setFormState] = useState(
    Object.fromEntries(fields.map((field) => [field.name, '']))
  );

  const navigate = useNavigate();
  const location = useLocation();

  const handleChange = (event) => {
    setFormState({
      ...formState,
      [event.target.name]: event.target.value,
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log('Submitting form:', formState);
    onSubmit(formState).then((response) => {
      if (response.status === 201) {
        navigate('/login');
      } else if (response.status === 200) {
        const jwtToken = getJwtFromCookie();

        if (jwtToken) {
          try {
            const decoded = jwt.decode(jwtToken);

            console.log('Decoded JWT payload:', decoded);
          } catch (error) {
            console.error('Error decoding JWT token:', error);
          }
        } else {
          console.log('JWT token not found in cookie');
        }
        navigate('/');
      }
    });
  };

  return (
    <form onSubmit={handleSubmit}>
      {fields.map((field) => (
        <React.Fragment key={field.name}>
          <label htmlFor={field.name}>{field.label}</label>
          <input
            type={field.type}
            id={field.name}
            name={field.name}
            value={formState[field.name]}
            onChange={handleChange}
          />
        </React.Fragment>
      ))}
      {includeRememberMe && (
        <div className='form__checkbox'>
          <input
            type='checkbox'
            name='rememberMe'
            value={formState.rememberMe}
            onChange={handleChange}
          />
          <label htmlFor='rememberMe'>Remember me</label>
        </div>
      )}
      <Button
        onClick={handleSubmit}
        Icon={null}
        iconClassName={null}
        className={'form__main--call_to-action'}
      >
        {submitText}
      </Button>
      {includeSocial && (
        <div className='form__social-media--buttons'>
          <Button
            onClick={handleSubmit}
            Icon={null}
            iconClassName={null}
            className={'form__social-media--button'}
          >
            {location.pathname.includes('register')
              ? 'Register with Facebook'
              : 'Log in with Facebook'}
          </Button>
          <Button
            onClick={handleSubmit}
            Icon={null}
            iconClassName={null}
            className={'form__social-media--button'}
          >
            {location.pathname.includes('register')
              ? 'Register with Google'
              : 'Log in with Google'}
          </Button>
        </div>
      )}
    </form>
  );
};

export default Form;
