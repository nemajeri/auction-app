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
  TermsAndCondPage,
  AboutPage,
  PrivacyPolicyPage,
  ShopPage,
  LandingPage,
  ProductOverviewPage,
  LoginPage,
  RegisterPage,
  MyAccountPage,
  SellPage,
} from './pages/index';
import { Route, Routes } from 'react-router-dom';
import { AppContextProvider } from './utils/AppContextProvider';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import  NotLoggedInRoute from './utils/routes/NotLoggedInRoute';
import  LoggedInRoute from './utils/routes/LoggedInRoute';

function App() {
  return (
    <>
      <AppContextProvider>
        <Navbar />
        <ToastContainer />
        <Routes>
          <Route
            path={shopPagePathToProduct}
            element={<ProductOverviewPage />}
          />
          <Route path={`${myAccountPath}/*`} element={
            <LoggedInRoute>
              <MyAccountPage />
            </LoggedInRoute>
          } />
          <Route path={shopPagePath} element={<ShopPage />} />
          <Route path={shopPagePathToCategory} element={<ShopPage />} />
          <Route
            path={productOverviewPagePath}
            element={<ProductOverviewPage />}
          />
          <Route path={loginPath} element={
            <NotLoggedInRoute>
              <LoginPage />
            </NotLoggedInRoute>
          } />
          <Route path={registrationPath} element={
            <NotLoggedInRoute>
              <RegisterPage />
            </NotLoggedInRoute>
          } />
          <Route path={landingPagePath} element={<LandingPage />} />
          <Route path={aboutUsPath} element={<AboutPage />} />
          <Route path={privacyPolicyPath} element={<TermsAndCondPage />} />
          <Route path={termsAndCondPath} element={<PrivacyPolicyPage />} />
          <Route path={sellerToAddItemPath} element={
            <LoggedInRoute>
              <SellPage />
            </LoggedInRoute>
          } />
        </Routes>
        <Footer />
      </AppContextProvider>
    </>
  );
}

export default App;
