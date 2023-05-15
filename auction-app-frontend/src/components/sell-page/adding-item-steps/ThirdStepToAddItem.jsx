import React from 'react';
import Form from '../../../utils/forms/Form';
import Button from '../../../utils/Button';

import { Link } from 'react-router-dom';
import { getStep3Fields } from '../../../data/multiformfields';

const ThirdStepToAddItem = ({
  prevStep,
  setProductDetails,
  handleFinalSubmit,
  sellerPath,
  errors,
  setErrors,
  initialValues,
}) => {
  const fields = getStep3Fields();

  return (
    <div className='shared-form-style__container'>
      <div className='shared-form-style__headline'>
        <h3>LOCATION & SHIPPING</h3>
      </div>
      <div className='shared-form-style__wrapper'>
        <Form
          errors={errors}
          setErrors={setErrors}
          fields={fields}
          onFormStateChange={(newState) =>
            setProductDetails({ ...initialValues, ...newState })
          }
          initialValues={initialValues}
        >
          <div className='shared-form-style__card-section'>
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
                  onClick={(e) => handleFinalSubmit(e)}
                >
                  Done
                </Button>
              </div>
            </div>
          </div>
        </Form>
      </div>
    </div>
  );
};

export default ThirdStepToAddItem;
