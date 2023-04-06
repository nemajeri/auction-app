import './App.css';
import Navbar from './layout/navbar/Navbar';
import Footer from './layout/footer/Footer';
import {
  aboutUsPath,
  privacyPolicyPath,
  termsAndCondPath,
  landingPagePath,
  productOverviewPagePath,
} from './utils/paths';
import {
  TermsAndCondPage,
  AboutPage,
  PrivacyPolicyPage,
  LandingPage,
  ProductOverviewPage,
} from './pages/index';
import { Route, Routes } from 'react-router-dom';
import ScrollToTop from './utils/ScrollToTop';

function App() {
  return (
    <>
    <ScrollToTop />
      <Navbar />  
        <Routes>
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
    </>
  );
}

export default App;
