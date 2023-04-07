import './App.css';
import Navbar from './layout/navbar/Navbar';
import Footer from './layout/footer/Footer';
import {
  aboutUsPath,
  privacyPolicyPath,
  termsAndCondPath,
  shopPagePath,
  landingPagePath,
  productOverviewPagePath,
} from './utils/paths';
import {
  TermsAndCondPage,
  AboutPage,
  PrivacyPolicyPage,
  ShopPage,
  LandingPage,
  ProductOverviewPage,
} from './pages/index';
import { Route, Routes } from 'react-router-dom';
import ScrollToTop from './utils/ScrollToTop';

function App() {
  return (
    <>
      <AppContextProvider>
        <ScrollToTop />
        <Navbar />
        <Routes>
          <Route path={shopPagePath} element={<ShopPage />} />
          <Route
            path={productOverviewPagePath}
            element={<ProductOverviewPage />}
          />
          <Route path={landingPagePath} element={<LandingPage />} />
          <Route path={aboutUsPath} element={<AboutPage />} />
          <Route path={privacyPolicyPath} element={<TermsAndCondPage />} />
          <Route path={termsAndCondPath} element={<PrivacyPolicyPage />} />
        </Routes>
        <Footer />
      </AppContextProvider>
    </>
  );
}

export default App;
