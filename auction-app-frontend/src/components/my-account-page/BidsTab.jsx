import React from 'react';
import { RiAuctionFill } from 'react-icons/ri';
import AuctionTable from '../auction-table/AuctionTable';
import CallToAction from '../CallToAction';

const BidsTab = ({ headings, headerClassNames = [] }) => {
  return (
    <>
      <AuctionTable headings={headings} headerClassNames={headerClassNames}>
        <tr>
          <td>
            <CallToAction
              icon={<RiAuctionFill className='shared-style--icon' />}
              text='You don’t have any bids and there are so many cool products available for sale.'
              buttonLabel='START BIDDING'
              buttonClassName='shared-style__btn'
            />
          </td>
        </tr>
      </AuctionTable>
    </>
  );
};

export default BidsTab;
