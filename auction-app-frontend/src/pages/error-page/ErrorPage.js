import React from 'react';
import Button from '../../utils/Button';
import './error-page.css';
import Logo from '../../assets/Logo';
import { useNavigate } from 'react-router-dom';

const ErrorPage = () => {
  const navigate = useNavigate();
  return (
    <>
      <div className='error-page__logo'>
        <Logo />
      </div>
      <div className='wrapper error-page__wrapper'>
        <div className='content'>
          <h1>404</h1>
          <h5>Ooops! Looks like the page is not Found</h5>
          <Button onClick={() => navigate(-1)} className={'error-page__btn'}>Go Back</Button>
        </div>
      </div>
    </>
  );
};

export default ErrorPage;
