import '@testing-library/jest-dom';
import { render, screen } from '@testing-library/react';
import AuctionTable from './AuctionTable';

describe('AuctionTable', () => {
  it('renders headings and children correctly', () => {
    const headings = ['Heading 1', 'Heading 2'];
    const childText = 'Child component';

    render(
      <AuctionTable headings={headings}>
        <tr>
          <td>{childText}</td>
        </tr>
      </AuctionTable>
    );

    headings.forEach(heading => {
      expect(screen.getByText(heading)).toBeInTheDocument();
    });
    
    expect(screen.getByText(childText)).toBeInTheDocument();
  });
});