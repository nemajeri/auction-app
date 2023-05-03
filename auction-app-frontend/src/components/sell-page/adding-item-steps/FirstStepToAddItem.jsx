import React, { useState, useEffect } from 'react';
import Form from '../../../utils/forms/Form';
import Dropzone from '../../../utils/Dropzone';
import Button from '../../../utils/Button';
import { getCategories } from '../../../utils/api/categoryApi';
import { getSubcategories } from '../../../utils/api/subcategoryApi';
import { Link, useLocation } from 'react-router-dom';

const FirstStepToAddItem = ({ nextStep, setStep1State, sellerPath }) => {
  const [categories, setCategories] = useState([]);
  const [subcategories, setSubcategories] = useState([]);

  const location = useLocation();
  console.log('Pathname: ',location.pathname)

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await getCategories();
        setCategories(response.data);
      } catch (error) {
        console.error('Error fetching categories:', error.message);
      }
    };

    fetchCategories();
  }, []);

  useEffect(() => {
    const fetchSubcategories = async (categoryId) => {
      try {
        const response = await getSubcategories(categoryId);
        setSubcategories(response.data);
      } catch (error) {
        console.error('Error fetching subcategories:', error.message);
      }
    };

    if (categories.length > 0) {
      fetchSubcategories(categories[0].id);
    }
  }, [categories]);

  const categoryOptions = categories.map((category) => ({
    label: category.categoryName,
    value: category.id,
  }));

  const subcategoryOptions = subcategories.map((subcategory) => ({
    label: subcategory.subCategoryName,
    value: subcategory.id,
  }));

  const fields = [
    {
      name: 'productName',
      label: 'What do you sell?',
      type: 'text',
      placeholder: 'eg. Targeal 7.1 Surround Sound Gaming Headset for PS4',
    },
    {
      className: 'shared-form__flex',
      fields: [
        {
          name: 'category',
          type: 'select',
          options: categoryOptions,
          placeholder: 'Select Category',
        },
        {
          name: 'subcategory',
          type: 'select',
          options: subcategoryOptions,
          placeholder: 'Select Subcategory',
        },
      ],
    },
    {
      name: 'description',
      label: 'Description',
      type: 'textarea',
    },
  ];

  return (
    <div className='shared-form-style__wrapper'>
      <div className='shared-form-style__headline'>
        <h3>ADD ITEM</h3>
      </div>
      <Form
        fields={fields}
        onFormStateChange={(newState) => setStep1State(newState)}
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
