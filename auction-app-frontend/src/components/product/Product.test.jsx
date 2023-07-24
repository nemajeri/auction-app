import '@testing-library/jest-dom';
import Product from './Product';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';

describe('Product', () => {
  it('renders correctly', () => {
    const product = {
      id: 1,
      productName: 'Samsung TV',
      startPrice: 1480,
      images: ['pathToImg/productImg1.jpg', 'pathToImg/productImg2.jpg'],
    };
    render(<Product product={product} location={'shop'} />, {
      wrapper: BrowserRouter,
    });

    const productName = screen.getByText('Samsung TV');
    expect(productName).toBeInTheDocument();

    const priceRegex = new RegExp(`${product.startPrice.toFixed(2)}`);
    const startPrice = screen.getByText(priceRegex);
    expect(startPrice).toBeInTheDocument();

    const productImage = screen.getByAltText('Samsung TV');
    expect(productImage).toBeInTheDocument();
    expect(productImage).toHaveAttribute('src', 'pathToImg/productImg1.jpg');
  });
});
