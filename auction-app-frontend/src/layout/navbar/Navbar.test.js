import { render, screen } from '@testing-library/react';
import Navbar from './Navbar';
import '@testing-library/jest-dom';
import { AppContext } from '../../utils/AppContextProvider';
import { BrowserRouter } from 'react-router-dom';

jest.mock('../../services/WebSocketService', () => {
  class MockWebSocketService {
    connect = jest.fn();
    disconnect = jest.fn();
    subscribe = jest.fn();
    send = jest.fn();
  }
  return {
    __esModule: true,
    default: MockWebSocketService,
  };
});

describe('Navbar', () => {
  const onSearchTermChange = jest.fn();

  it('renders logged in user', () => {
    const mockContextValue = {
      searchTerm: '',
      suggestion: '',
      activeCategory: null,
      user: { id: 1, firstName: 'John', lastName: 'Doe' },
      isClearButtonPressed: false,
      notifications: [],
      onSearchTermChange,
      notificationsWebSocketServiceRef: { current: null },
      notificationsSubscriptionRef: { current: null },
    };

    render(
      <BrowserRouter>
        <AppContext.Provider value={mockContextValue}>
          <Navbar />
        </AppContext.Provider>
      </BrowserRouter>
    );
    expect(screen.getByText(/Hi,\s*John\s*Doe/s)).toBeInTheDocument();
  });
});
