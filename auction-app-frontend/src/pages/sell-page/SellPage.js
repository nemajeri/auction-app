import React, { useState, useEffect, useContext } from 'react';
import BreadCrumbs from '../../components/breadcrumbs/Breadcrumbs';
import './sellpage.css';
import { addNewItemForAuction } from '../../utils/api/productsApi';
import { validateFormFields } from '../../utils/helperFunctions';
import {
  getStep1Fields,
  getStep2Fields,
  getStep3Fields,
} from '../../data/multiformfields';
import { getCategories } from '../../utils/api/categoryApi';
import { getSubcategories } from '../../utils/api/subcategoryApi';
import { AppContext } from '../../utils/AppContextProvider';
import { useNavigate } from 'react-router-dom';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import { toast } from 'react-toastify';
import RenderProgressDots from '../../components/sell-page/adding-item-steps/RenderProgressDots';
import MultiStepForm from '../../components/sell-page/adding-item-steps/Multiform';
import { sellerPath } from '../../utils/paths';
import { EMPTY_STRING } from '../../utils/constants';
import axios from 'axios';

const SellPage = () => {
  const [step, setStep] = useState(1);
  const [errors, setErrors] = useState({});
  const [categories, setCategories] = useState([]);
  const [subcategories, setSubcategories] = useState([]);
  const { user } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const [images, setImages] = useState([]);
  const [productDetails, setProductDetails] = useState({
    productName: EMPTY_STRING,
    description: EMPTY_STRING,
    categoryId: EMPTY_STRING,
    subcategoryId: EMPTY_STRING,
    startPrice: EMPTY_STRING,
    startDate: EMPTY_STRING,
    endDate: EMPTY_STRING,
    address: user?.address || EMPTY_STRING,
    city: user?.city || EMPTY_STRING,
    zipCode: user?.zipCode || EMPTY_STRING,
    country: user?.country || EMPTY_STRING,
    phone: user?.phone || EMPTY_STRING,
  });

  const navigate = useNavigate();

  useEffect(() => {
    const CancelToken = axios.CancelToken;
    const source = CancelToken.source();

    (async () => {
      setLoading(true);
      try {
        const response = await getCategories(source.token);
        let i = response.data.length - 1;
        response.data.splice(i, 1);
        setCategories(response.data);
      } catch (error) {
        if (axios.isCancel(error)) {
          console.error('Request cancelled:', error.message);
          return;
        } else {
          console.error("Error fetching categories: " + error);
        }
      } finally {
        setLoading(false);
      }
    })();

    return () => {
      source.cancel('Operation cancelled by the user.');
    };
  }, []);

  const updateSubcategories = async (categoryId) => {
    try {
      const response = await getSubcategories(categoryId);
      setSubcategories(response.data);
    } catch (error) {
      console.error('Error fetching subcategories:', error.message);
    }
  };

  const categoryOptions = categories.map((category) => ({
    label: category.categoryName,
    value: category.id,
  }));

  const subcategoryOptions = subcategories.map((subcategory) => ({
    label: subcategory.subCategoryName,
    value: subcategory.id,
  }));

    const nextStep = () => {
      setStep(step + 1);
    };

    const prevStep = () => {
      setStep(step - 1);
    };

  const firstStepToAddItemFields = getStep1Fields(
    categoryOptions,
    subcategoryOptions
  );
  const secondStepToAddItemFields = getStep2Fields();
  const thirdStepToAddItemFields = getStep3Fields();

  const allFields = [
    ...firstStepToAddItemFields,
    ...secondStepToAddItemFields,
    ...thirdStepToAddItemFields,
  ];

  const handleFinalSubmit = async (e) => {
    e.preventDefault();

    const errors = validateFormFields(productDetails, allFields);

    const hasErrors = Object.values(errors).some(
      (error) => error !== undefined
    );

    if (images.length < 3) {
      toast.error('You need to add at least 3 images.');
    }

    if (hasErrors) {
      toast.error('Please fill in all required fields.');
    } else {
      setLoading(true);
      addNewItemForAuction(productDetails, images, setLoading, navigate);
    }
  };

  return (
    <>
      <BreadCrumbs title='SELLER' />
      <RenderProgressDots step={step} />
      <div className='shared-form_position'>
        <MultiStepForm
          step={step}
          nextStep={nextStep}
          prevStep={prevStep}
          handleFinalSubmit={handleFinalSubmit}
          productDetails={productDetails}
          setProductDetails={setProductDetails}
          errors={errors}
          setErrors={setErrors}
          categoryOptions={categoryOptions}
          subcategoryOptions={subcategoryOptions}
          updateSubcategories={updateSubcategories}
          images={images}
          setImages={setImages}
          sellerPath={sellerPath}
        />
      </div>
      {loading && <LoadingSpinner pageSpinner={true}/>}
    </>
  );
};

export default SellPage;
