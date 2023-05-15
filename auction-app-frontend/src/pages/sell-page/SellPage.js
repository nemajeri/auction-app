import React, { useState, useEffect, useContext } from 'react';
import {
  FirstStepToAddItem,
  SecondStepToAddItem,
  ThirdStepToAddItem,
} from '../../components/sell-page/adding-item-steps';
import BreadCrumbs from '../../components/breadcrumbs/Breadcrumbs';
import './sellpage.css';
import { addNewItemForAuction } from '../../utils/api/productsApi';
import Modal from '../../utils/forms/Modal.jsx';
import { sellerPath } from '../../utils/paths';
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
import LoadingSpinner  from '../../components/loading-spinner/LoadingSpinner';

const SellPage = () => {
  const [step, setStep] = useState(1);
  const [showModal, setShowModal] = useState(false);
  const [errors, setErrors] = useState({});
  const [categories, setCategories] = useState([]);
  const [subcategories, setSubcategories] = useState([]);
  const [modalMessage, setModalMessage] = useState('');
  const { user, loading, setLoading } = useContext(AppContext);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await getCategories();
        let i = response.data.length - 1;
        response.data.splice(i, 1);
        setCategories(response.data);
      } catch (error) {
        console.error('Error fetching categories:', error.message);
      }
    };

    fetchCategories();
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
            productDetails={productDetails}
            sellerPath={sellerPath}
            errors={errors}
            setErrors={setErrors}
            setProductDetails={setProductDetails}
            categoryOptions={categoryOptions}
            subcategoryOptions={subcategoryOptions}
            updateSubcategories={updateSubcategories}
            initialValues={productDetails}
            setImages={setImages}
            images={images}
          />
        );
      case 2:
        return (
          <SecondStepToAddItem
            nextStep={nextStep}
            prevStep={prevStep}
            productDetails={productDetails}
            sellerPath={sellerPath}
            errors={errors}
            setErrors={setErrors}
            setProductDetails={setProductDetails}
            initialValues={productDetails}
          />
        );
      case 3:
        return (
            <ThirdStepToAddItem
              prevStep={prevStep}
              productDetails={productDetails}
              handleFinalSubmit={handleFinalSubmit}
              sellerPath={sellerPath}
              errors={errors}
              setErrors={setErrors}
              setProductDetails={setProductDetails}
              initialValues={productDetails}
            />
        );
      default:
        return null;
    }
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
      setModalMessage('You need to add at least 3 images.');
      setShowModal(true);
      return;
    }

    if (hasErrors) {
      setModalMessage('Please fill in all required fields.');
      setShowModal(true);
      setErrors(errors);
    } else {
      setLoading(true);
      addNewItemForAuction(
        productDetails,
        images,
        setShowModal,
        setModalMessage,
        setLoading
      );
    }
  };

  const onClose = () => {
    setShowModal(false);
    navigate(sellerPath);
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
      <Modal showModal={showModal} onClose={onClose}>
        {modalMessage}
      </Modal>
      {loading && <LoadingSpinner />}
    </>
  );
};

export default SellPage;
