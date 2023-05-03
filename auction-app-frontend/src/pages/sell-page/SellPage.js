import React, { useState } from 'react';
import {
  FirstStepToAddItem,
  SecondStepToAddItem,
  ThirdStepToAddItem,
} from '../../components/sell-page/adding-item-steps';
import BreadCrumbs from '../../components/breadcrumbs/Breadcrumbs';
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import './sellpage.css';
import { addNewItemForAuction } from '../../utils/api/productsApi';
import Modal from '../../utils/forms/Modal.jsx';
import { sellerPath } from '../../utils/paths';
import { useLocation } from 'react-router-dom';

const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLISHED_KEY);

const SellPage = () => {
  const [step, setStep] = useState(1);
  const [showModal, setShowModal] = useState(false);

  const location = useLocation();
  console.log('Pathname: ',location.pathname)

  const [step1State, setStep1State] = useState({
    productName: '',
    description: '',
    categoryId: '',
    subcategoryId: '',
  });

  const [step2State, setStep2State] = useState({
    startPrice: '',
    startDate: '',
    endDate: '',
  });


  const [step3State, setStep3State] = useState({
    address: '',
    city: '',
    zipCode: '',
    country: '',
    phone: '',
  });

  const MultiStepForm = () => {
    const nextStep = () => {
      setStep(step + 1);
    };

    const prevStep = () => {
      setStep(step - 1);
    };

    switch (step) {
      case 1:
        return (
          <FirstStepToAddItem
            nextStep={nextStep}
            step1State={step1State}
            setStep1State={setStep1State}
            sellerPath={sellerPath}
          />
        );
      case 2:
        return (
          <SecondStepToAddItem
            nextStep={nextStep}
            prevStep={prevStep}
            step2State={step2State}
            setStep2State={setStep2State}
            sellerPath={sellerPath}
          />
        );
      case 3:
        return (
          <Elements stripe={stripePromise}>
            <ThirdStepToAddItem
              prevStep={prevStep}
              step3State={step3State}
              setStep3State={setStep3State}
              handleFinalSubmit={handleFinalSubmit}
              sellerPath={sellerPath}
            />
          </Elements>
        );
      default:
        return null;
    }
  };

  const formatFormData = () => {
    return {
      productName: step1State.productName,
      description: step1State.description,
      categoryId: 8,
      subcategoryId: 1,
      startPrice: step1State.startPrice,
      startDate: step2State.startDate,
      endDate: step2State.endDate,
      address: step3State.address,
      city: step3State.city,
      zipCode: step3State.zipCode,
      country: step3State.country,
      phone: step3State.phoneNumber,
    };
  };

  const handleFinalSubmit = async (e, stripe, elements) => {
    e.preventDefault();
    if (!stripe || !elements) {
      return;
    }

    const formData = formatFormData();

    addNewItemForAuction(formData, setShowModal);
  };

  const renderProgressDots = () => {
    const totalSteps = 3;
    const dots = [];

    for (let i = 1; i <= totalSteps; i++) {
      dots.push(
        <div className='sell-page__progress-dot_circle' key={i}>
          <div
            className={`sell-page__progress-dot ${
              i <= step ? 'sell-page__progress-active_dot' : ''
            }`}
          />
        </div>
      );
    }

    return (
      <div className='sell-page__progress-container'>
        <div className='sell-page__progress-line' />
        {dots}
      </div>
    );
  };

  return (
    <>
      <BreadCrumbs title='SELLER' />
      {renderProgressDots()}
      <div className='shared-form_position'>{MultiStepForm()}</div>
      <Modal
        showModal={showModal}
        successPath={sellerPath}
      />
    </>
  );
};

export default SellPage;
