import { useState } from 'react';
import { FiShoppingCart } from 'react-icons/fi';
import Tabs from '../tabs/Tabs';
import AuctionTable from '../auction-table/AuctionTable';
import CallToAction from '../CallToAction';

const tabs = [
  { id: 'active', label: 'Active' },
  { id: 'sold', label: 'Sold' },
];

const SellerTab = ({ headings, headerClassNames }) => {
  const [selectedTab, setSelectedTab] = useState(tabs[0].id);

  const handleTabClick = (id) => {
    setSelectedTab(id);
  };

  return (
    <>
      <Tabs
        tabs={tabs}
        handleTabClick={handleTabClick}
        selectedTab={selectedTab}
        containerClassName={'seller-tab__tabs--container'}
        tabClassName={'seller-tab__tabs--individual_tab'}
        selectedTabClassName={'seller-tab__tabs--individual_tab-selected'}
      />
      <AuctionTable headings={headings} headerClassNames={headerClassNames}>
        <tr>
          <td>
            <CallToAction
              icon={<FiShoppingCart className='shared-style--icon' />}
              text='You do not have any scheduled items for sale.'
              buttonLabel='START SELLING'
              buttonClassName='shared-style__btn'
            />
          </td>
        </tr>
      </AuctionTable>
    </>
  );
};

export default SellerTab;
