import React from 'react';
import FirstStepToAddItem from './FirstStepToAddItem';
import SecondStepToAddItem from './SecondStepToAddItem';
import ThirdStepToAddItem from './ThirdStepToAddItem';

const MultiStepForm = ({
  step,
  nextStep,
  prevStep,
  handleFinalSubmit,
  productDetails,
  setProductDetails,
  errors,
  setErrors,
  categoryOptions,
  subcategoryOptions,
  updateSubcategories,
  images,
  setImages,
  sellerPath
}) => {
  const steps = [
    <FirstStepToAddItem
      nextStep={nextStep}
      productDetails={productDetails}
      errors={errors}
      setErrors={setErrors}
      setProductDetails={setProductDetails}
      categoryOptions={categoryOptions}
      subcategoryOptions={subcategoryOptions}
      updateSubcategories={updateSubcategories}
      initialValues={productDetails}
      setImages={setImages}
      images={images}
      sellerPath={sellerPath}
    />,
    <SecondStepToAddItem
      nextStep={nextStep}
      prevStep={prevStep}
      productDetails={productDetails}
      errors={errors}
      setErrors={setErrors}
      setProductDetails={setProductDetails}
      initialValues={productDetails}
      sellerPath={sellerPath}
    />,
    <ThirdStepToAddItem
      prevStep={prevStep}
      productDetails={productDetails}
      handleFinalSubmit={handleFinalSubmit}
      errors={errors}
      setErrors={setErrors}
      setProductDetails={setProductDetails}
      initialValues={productDetails}
      sellerPath={sellerPath}
    />,
  ];

  return <div>{steps[step - 1]}</div>;
};

export default MultiStepForm;