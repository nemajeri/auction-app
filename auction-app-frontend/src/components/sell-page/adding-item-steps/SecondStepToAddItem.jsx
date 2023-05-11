import React from 'react';
import Form from '../../../utils/forms/Form';
import Button from '../../../utils/Button';
import { Link } from 'react-router-dom';
import { getStep2Fields } from '../../../data/multiformfields';

const SecondStepToAddItem = ({
  nextStep,
  prevStep,
  setProductDetails,
  sellerPath,
  errors,
  setErrors,
  initialValues,
}) => {
  const fields = getStep2Fields(initialValues);

  return (
    <div className='shared-form-style__container'>
      <div className='shared-form-style__headline'>
        <h3>SET PRICES</h3>
      </div>
      <div className='shared-form-style__wrapper'>
        <Form
          fields={fields}
          onFormStateChange={(newState) =>
            setProductDetails({ ...initialValues, ...newState })
          }
          errors={errors}
          setErrors={setErrors}
          initialValues={initialValues}
        >
          <p className='shared-form-style__hint'>
            This auction will be automatically closed when the end time comes.
            The highest bid will win the auction
          </p>
          <div className='shared-form-style__btns navigation'>
            <Link to={sellerPath}>
              <Button className={'shared-form-style__btn cancel-btn__short'}>
                Cancel
              </Button>
            </Link>
            <div className='shared-form-style__btns main-navigation'>
              <Button
                className={'shared-form-style__btn back-btn'}
                onClick={prevStep}
              >
                Back
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
    </div>
  );
};

export default SecondStepToAddItem;
