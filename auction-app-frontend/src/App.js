import './App.css';
import Navbar from './layout/navbar/Navbar';
import Footer from './layout/footer/Footer';
import { landingPagePath } from './utils/paths';
import {
  Route,
  Routes,
  Link,
  createBrowserRouter,
  RouterProvider,
} from 'react-router-dom';
import { LandingPage } from './pages/index';

function App() {

  const router = createBrowserRouter([
    {
      path: landingPagePath,
      element: <LandingPage />,
      handle: { crumb: () => <Link to={`/${landingPagePath}`}>About Us</Link> },
    },
  ]);

  return (
    <>
      <Navbar />
      <RouterProvider router={router}>
          <Routes>
            <Route
              path={landingPagePath}
              element={<LandingPage />}
            />
          </Routes>
        </RouterProvider>
      <Footer />
    </>
  );
}

export default App;
