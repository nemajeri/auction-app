import './App.css';
import Navbar from './layout/navbar/Navbar';
import Footer from './layout/footer/Footer';
import {
  aboutUsPath,
  privacyPolicyPath,
  termsAndCondPath,
  shopPagePath,
} from './utils/paths';
import {
  TermsAndCondPage,
  AboutPage,
  PrivacyPolicyPage,
  ShopPage,
} from './pages/index';
import { Route, Routes } from 'react-router-dom';
import ScrollToTop from './utils/ScrollToTop';
import { AppContextProvider } from './utils/AppContextProvider';

function App() {
  return (
    <>
      <AppContextProvider>
        <ScrollToTop />
        <Navbar />
        <Routes>
          <Route path={shopPagePath} element={<ShopPage />} />
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
