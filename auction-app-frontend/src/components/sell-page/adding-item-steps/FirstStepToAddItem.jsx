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
  initialValues,
  setImages,
  images,
}) => {
  const fields = getStep1Fields(categoryOptions, subcategoryOptions);

  return (
    <div className='shared-form-style__container'>
      <div className='shared-form-style__headline'>
        <h3>ADD ITEM</h3>
      </div>
      <div className='shared-form-style__wrapper'>
        <Form
          errors={errors}
          setErrors={setErrors}
          fields={fields}
          onFormStateChange={(newState) =>
            setProductDetails({ ...initialValues, ...newState })
          }
          updateSubcategories={updateSubcategories}
          initialValues={initialValues}
        >
          <div className='shared-form-style__align--text_right'>
            <p>100 words (700 characters)</p>
            <Dropzone
              Dropzone
              onDrop={(acceptedFiles) =>
                setImages([...images, ...acceptedFiles])
              }
              images={images}
            />
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
    </div>
  );
};

export default FirstStepToAddItem;
