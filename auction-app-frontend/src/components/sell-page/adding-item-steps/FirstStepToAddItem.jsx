import React from 'react';
import Form from '../../../utils/forms/Form';
import Dropzone from '../../../utils/Dropzone';
import Button from '../../../utils/Button';
import { Link } from 'react-router-dom';
import { getStep1Fields } from '../../../data/multiformfields';

const FirstStepToAddItem = ({
  nextStep,
  setProductDetails,
  sellerPath,
  errors,
  setErrors,
  categoryOptions,
  subcategoryOptions,
  updateSubcategories,
  initialValues
}) => {

  const fields = getStep1Fields(categoryOptions, subcategoryOptions);

  return (
    <div className='shared-form-style__wrapper'>
      <div className='shared-form-style__headline'>
        <h3>ADD ITEM</h3>
      </div>
      <Form
        errors={errors}
        setErrors={setErrors}
        fields={fields}
        onFormStateChange={(newState) => setProductDetails({ ...initialValues, ...newState })}
        updateSubcategories={updateSubcategories}
        initialValues={initialValues}
      >
        <div className='shared-form-style__align--text_right'>
          <p>100 words (700 characters)</p>
          <Dropzone />
          <div className='shared-form-style__btns'>
            <Link to={sellerPath}>
              <Button className={'shared-form-style__btn cancel-btn__long'}>
                Cancel
              </Button>
            </Link>
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
  );
};

export default FirstStepToAddItem;
