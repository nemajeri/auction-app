import React, { lazy, Suspense } from 'react';
import './App.css';
import Navbar from './layout/navbar/Navbar';
import Footer from './layout/footer/Footer';
import {
  aboutUsPath,
  privacyPolicyPath,
  termsAndCondPath,
  shopPagePathToCategory,
  landingPagePath,
  productOverviewPagePath,
  shopPagePathToProduct,
  loginPath,
  registrationPath,
  myAccountPath,
  shopPagePath,
  sellerToAddItemPath,
} from './utils/paths';
import {
  AboutPage,
  PrivacyPolicyPage,
  LoginPage,
  RegisterPage,
} from './pages/index';
import { Route, Routes } from 'react-router-dom';
import { AppContextProvider } from './utils/AppContextProvider';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import NotLoggedInRoute from './utils/routes/NotLoggedInRoute';
import LoggedInRoute from './utils/routes/LoggedInRoute';
import LoadingSpinner from './components/loading-spinner/LoadingSpinner';

const LandingPage = lazy(() => import('./pages/landing-page/LandingPage.js'));
const ProductOverviewPage = lazy(() => import('./pages/product-overview-page/ProductOverviewPage.js'));
const TermsAndCondPage = lazy(() => import('./pages/terms-and-cond/TermsAndCondPage.js'));
const ShopPage = lazy(() => import('./pages/shop-page/ShopPage.js'));
const MyAccountPage = lazy(() => import('./pages/my-account-page/MyAccountPage.js'));
const SellPage = lazy(() => import('./pages/sell-page/SellPage.js'));


function App() {
  return (
    <>
      <AppContextProvider>
        <Navbar />
        <ToastContainer />
        <Routes>
          <Route
            path={shopPagePathToProduct}
            element={
              <Suspense fallback={<LoadingSpinner pageSpinner={true} />}>
                <ProductOverviewPage />{' '}
              </Suspense>
            }
          />
          <Route
            path={`${myAccountPath}/*`}
            element={
              <LoggedInRoute>
                <Suspense fallback={<LoadingSpinner pageSpinner={true} />}>
                  <MyAccountPage />
                </Suspense>
              </LoggedInRoute>
            }
          />
          <Route
            path={shopPagePath}
            element={
              <Suspense fallback={<LoadingSpinner pageSpinner={true} />}>
                <ShopPage />
              </Suspense>
            }
          />
          <Route
            path={shopPagePathToCategory}
            element={
              <Suspense fallback={<LoadingSpinner pageSpinner={true} />}>
                <ShopPage />
              </Suspense>
            }
          />
          <Route
            path={productOverviewPagePath}
            element={
              <Suspense fallback={<LoadingSpinner pageSpinner={true} />}>
                <ProductOverviewPage />
              </Suspense>
            }
          />
          <Route
            path={loginPath}
            element={
              <NotLoggedInRoute>
                <LoginPage />
              </NotLoggedInRoute>
            }
          />
          <Route
            path={registrationPath}
            element={
              <NotLoggedInRoute>
                <RegisterPage />
              </NotLoggedInRoute>
            }
          />
          <Route
            path={landingPagePath}
            element={
              <Suspense fallback={<LoadingSpinner pageSpinner={true} />}>
                <LandingPage />
              </Suspense>
            }
          />
          <Route path={aboutUsPath} element={<AboutPage />} />
          <Route
            path={privacyPolicyPath}
            element={
              <Suspense fallback={<LoadingSpinner pageSpinner={true} />}>
                <TermsAndCondPage />
              </Suspense>
            }
          />
          <Route path={termsAndCondPath} element={<PrivacyPolicyPage />} />
          <Route
            path={sellerToAddItemPath}
            element={
              <LoggedInRoute>
                <Suspense fallback={<LoadingSpinner pageSpinner={true} />}>
                  <SellPage />
                </Suspense>
              </LoggedInRoute>
            }
          />
        </Routes>
        <Footer />
      </AppContextProvider>
    </>
  );
}

export default App;
