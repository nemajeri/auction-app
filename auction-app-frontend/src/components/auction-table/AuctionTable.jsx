import React from 'react';
import './auctionTable.css';

const AuctionTable = ({ children, headings, headerClassNames = []}) => {
  return (
    <table>
      <thead>
        <tr>
          {headings.map((heading, index) => (
            <th key={index} className={headerClassNames[index]}>
              {heading}
            </th>
          ))}
        </tr>
      </thead>
      <tbody>{children}</tbody>
    </table>
  );
};

export default AuctionTable;