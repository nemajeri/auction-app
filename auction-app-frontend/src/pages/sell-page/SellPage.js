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

const SellPage = () => {
  const [step, setStep] = useState(1);
  const [errors, setErrors] = useState({});
  const [categories, setCategories] = useState([]);
  const [subcategories, setSubcategories] = useState([]);
  const { user } = useContext(AppContext);
  const [loading, setLoading] = useState(false);
  const [images, setImages] = useState([]);
  const [productDetails, setProductDetails] = useState({
    productName: '',
    description: '',
    categoryId: '',
    subcategoryId: '',
    startPrice: '',
    startDate: '',
    endDate: '',
    address: user?.address || '',
    city: user?.city || '',
    zipCode: user?.zipCode || '',
    country: user?.country || '',
    phone: user?.phone || '',
  });

  const navigate = useNavigate();

  useEffect(() => {
    (async () => {
      try {
        const response = await getCategories();
        let i = response.data.length - 1;
        response.data.splice(i, 1);
        setCategories(response.data);
      } catch (error) {
        console.error('Error fetching categories:', error.message);
      } finally {
        setLoading(false);
      }
    })();
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
      {loading && <LoadingSpinner />}
    </>
  );
};

export default SellPage;
