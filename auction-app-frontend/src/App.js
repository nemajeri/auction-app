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

function App() {

  const router = createBrowserRouter([
    {
      path: productOverviewPagePath,
      element: <ProductOverviewPage />,
      handle: { crumb: () => <Link to={`/${productOverviewPagePath}`}>About Us</Link> },
    },
  ]);

  return (
    <>
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
    </>
  );
}

export default App;
