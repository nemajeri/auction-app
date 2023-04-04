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
import {
  Route,
  Routes,
  Link,
  createBrowserRouter,
  RouterProvider,
  ScrollRestoration,
} from 'react-router-dom';
import { useBreadcrumbs } from './hooks/useBreadcrumbs';
import { NavigationProvider } from './utils/NavigationProvider';

function App() {
  const ProductOverviewPageWithBreadcrumbs =
    useBreadcrumbs(ProductOverviewPage);
  const AboutPageWithBreadcrumbs = useBreadcrumbs(AboutPage);
  const TermsAndCondPageWithBreadcrumbs = useBreadcrumbs(TermsAndCondPage);
  const PrivacyPolicyPageWithBreadcrumbs = useBreadcrumbs(PrivacyPolicyPage);

  const router = createBrowserRouter([
    {
      path: productOverviewPagePath,
      element: <ProductOverviewPageWithBreadcrumbs />,
      handle: {
        crumb: () => (
          <Link to={`/${productOverviewPagePath}`}>Example Product</Link>
        ),
      },
    },
    {
      path: landingPagePath,
      element: <LandingPage />,
      handle: { crumb: () => <Link to={`/${landingPagePath}`}>About Us</Link> },
    },
    {
      path: aboutUsPath,
      element: <AboutPageWithBreadcrumbs />,
      handle: { crumb: () => <Link to={`/${aboutUsPath}`}>About Us</Link> },
    },
    {
      path: privacyPolicyPath,
      element: <TermsAndCondPageWithBreadcrumbs />,
      handle: {
        crumb: () => (
          <Link to={`/${privacyPolicyPath}`}>Terms and Conditions</Link>
        ),
      },
    },
    {
      path: termsAndCondPath,
      element: <PrivacyPolicyPageWithBreadcrumbs />,
      handle: {
        crumb: () => <Link to={`/${termsAndCondPath}`}>Privacy Policy</Link>,
      },
    },
  ]);

  return (
    <>
      <NavigationProvider>
        <Navbar />
        <RouterProvider router={router}>
          <Routes>
            <Route
              path={productOverviewPagePath}
              element={<ProductOverviewPageWithBreadcrumbs />}
            />
            <Route path={landingPagePath} element={<LandingPage />} />
            <Route path={aboutUsPath} element={<AboutPageWithBreadcrumbs />} />
            <Route
              path={privacyPolicyPath}
              element={<TermsAndCondPageWithBreadcrumbs />}
            />
            <Route
              path={termsAndCondPath}
              element={<PrivacyPolicyPageWithBreadcrumbs />}
            />
          </Routes>
          <ScrollRestoration />
        </RouterProvider>
        <Footer />
      </NavigationProvider>
    </>
  );
}

export default App;
