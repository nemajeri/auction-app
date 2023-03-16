import './App.css';
import Navbar from './layout/navbar/Navbar';
import Footer from './layout/footer/Footer';
import {
  aboutUsPath,
  privacyPolicyPath,
  termsAndCondPath,
} from './utils/paths';
import { TermsAndCondPage, AboutPage, PrivacyPolicyPage } from './pages/index';
import {
  Route,
  Routes,
  Link,
  createBrowserRouter,
  RouterProvider,
} from 'react-router-dom';
import { useBreadcrumbs } from './hooks/useBreadcrumbs';
import { NavigationProvider } from './utils/NavigationProvider';

function App() {
  const AboutPageWithBreadcrumbs = useBreadcrumbs(AboutPage);
  const TermsAndCondPageWithBreadcrumbs = useBreadcrumbs(TermsAndCondPage);
  const PrivacyPolicyPageWithBreadcrumbs = useBreadcrumbs(PrivacyPolicyPage);

  const router = createBrowserRouter([
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
        </RouterProvider>
        <Footer />
      </NavigationProvider>
    </>
  );
}

export default App;
