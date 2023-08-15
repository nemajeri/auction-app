import * as React from 'react'
import { render, screen } from '@testing-library/react';
import ProductDetails from './ProductDetails';
import { BrowserRouter } from 'react-router-dom';
import '@testing-library/jest-dom/extend-expect';

jest.mock('../../../services/WebSocketService', () => {
  class MockWebSocketService {
    connect = jest.fn();
    disconnect = jest.fn();
    subscribe = jest.fn((destination, callback) => {
      if (destination === `/topic/watching/count`) {
        setTimeout(() => {
          callback({ body: JSON.stringify(1) });
        }, 0);
      }
      return { unsubscribe: jest.fn() };
    });
    send = jest.fn();
    stompClient = {
      connected: true,
      activate: jest.fn(),
      deactivate: jest.fn(),
      publish: jest.fn(),
      subscribe: jest.fn().mockReturnValue({ unsubscribe: jest.fn() }),
    };
  }
  return {
    __esModule: true,
    default: MockWebSocketService,
  };
});

describe('ProductDetails', () => {
  const mockProps = {
    watchersWebSocketServiceRef: { current: null },
    watchersSubscriptionRef: { current: null },
    user: { id: 1, firstName: 'John', lastName: 'Doe' },
    product: { id: 4, startPrice: 140, highestBid: 180 },
    tabs: [{ id: 2 }],
  };

  it('should change the value of watchers when user lands on page', async () => {
    const setWatchersCount = jest.fn();
    jest.spyOn(React, 'useState').mockImplementationOnce(stateValue => [stateValue = 1, setWatchersCount])

    render(
      <BrowserRouter>
        <ProductDetails {...mockProps} />
      </BrowserRouter>
    );

    const watcherButton = await screen.findByRole('button', {
      name: /watching 1/i, 
    });

    expect(watcherButton).toBeInTheDocument();
  });
});
