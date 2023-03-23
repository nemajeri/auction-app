import './App.css';
import Navbar from './layout/navbar/Navbar';
import Footer from './layout/footer/Footer';
import { productOverviewPagePath } from './utils/paths';
import {
  Route,
  Routes,
  Link,
  createBrowserRouter,
  RouterProvider,
} from 'react-router-dom';
import { ProductOverviewPage } from './pages/index';
import { useBreadcrumbs } from './hooks/useBreadcrumbs';
import { NavigationProvider } from './utils/NavigationProvider';

function App() {
  const ProductOverviewPageWithBreadcrumbs =
    useBreadcrumbs(ProductOverviewPage);

  const router = createBrowserRouter([
    {
      path: productOverviewPagePath,
      element: <ProductOverviewPageWithBreadcrumbs />,
      handle: {
        crumb: () => <Link to={`/${productOverviewPagePath}`}>BIYLACLESEN Womens 3-in-1 Snowboard Jacket Winter Coats</Link>, //need to change this hardcoded value using 
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
              element={<ProductOverviewPage />}
            />
          </Routes>
        </RouterProvider>
        <Footer />
      </NavigationProvider>
    </>
  );
}

export default App;
