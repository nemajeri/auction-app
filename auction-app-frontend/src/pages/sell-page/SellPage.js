import React, { useState, useCallback } from 'react';
import {
  FirstStepToAddItem,
  SecondStepToAddItem,
  ThirdStepToAddItem,
} from '../../components/sell-page/adding-item-steps';
import BreadCrumbs from '../../components/breadcrumbs/Breadcrumbs';
import './sellpage.css';

const SellPage = () => {
  const [step, setStep] = useState(1);
  const [images, setImages] = useState([]);

    const [step1State, setStep1State] = useState({
      productName: "",
      description: "",
      categoryId: "",
      subcategoryId: "",
    });
  
    const [step2State, setStep2State] = useState({
      startPrice: "",
      startDate: new Date(),
      endDate: "",
    });
  
    const [step3State, setStep3State] = useState({
      address: "",
      city: "",
      zipCode: "",
      country: "",
      phone: "",
    });

  const onDrop = useCallback(
    (acceptedImages) => {
      setImages([...images, ...acceptedImages]);
    },
    [images]
  );

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
          />
        );
      case 2:
        return (
          <SecondStepToAddItem
            nextStep={nextStep}
            prevStep={prevStep}
            step2State={step2State}
            setStep2State={setStep2State}
          />
        );
      case 3:
        return (
          <ThirdStepToAddItem
            prevStep={prevStep}
            step3State={step3State}
            setStep3State={setStep3State}
          />
        );
      default:
        return null;
    }
  };

  const renderProgressDots = () => {
    const totalSteps = 3;
    const dots = [];

    for (let i = 1; i <= totalSteps; i++) {
      dots.push(
        <div
          key={i}
          className={`sell-page__progress-dot ${(i <= step) ? 'sell-page__progress-active_dot' : ''}`}
        />
      );
    }

    return <div className="sell-page__progress-container">{dots}</div>;
  };

  return (
    <>
      <BreadCrumbs title='SELLER' />
      {renderProgressDots()}
      {MultiStepForm()}
    </>
  );
};

export default SellPage;
