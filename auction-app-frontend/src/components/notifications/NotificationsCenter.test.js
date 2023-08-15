import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import NotificationsCenter from './NotificationsCenter';

describe('NotificationsCenter', () => {
  const mockDispatch = jest.fn();

  const defaultNotificationsProps = [
    {
      type: 'OUTBID',
      product: 'Smartwatch',
    },
    {
      type: 'AUCTION_FINISHED',
      product: 'Jeans',
    },
  ];

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('renders without notifications', () => {
    render(<NotificationsCenter notifications={[]} dispatch={mockDispatch} />);
    expect(screen.getByText('0')).toBeInTheDocument();
  });

  it('renders with notifications', () => {
    render(
      <NotificationsCenter
        notifications={defaultNotificationsProps}
        dispatch={mockDispatch}
      />
    );
    expect(screen.getByText('2')).toBeInTheDocument();
  });
});
