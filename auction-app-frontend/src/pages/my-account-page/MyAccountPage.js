import React, { useState } from 'react';
import './myAccountPage.css';
import BreadCrumbs from '../../components/breadcrumbs/Breadcrumbs';
import { myAccountTabs } from '../../utils/constants';
import Button from '../../utils/Button';
import { sellerPath, bidsPath, sellerToSellItemPath } from '../../utils/paths';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import SellerTab from '../../components/my-account-page/SellerTab';
import BidsTab from '../../components/my-account-page/BidsTab';
import { AiOutlinePlus } from 'react-icons/ai';

const bidHeadings = [
  'Item',
  'Name',
  'Time left',
  'Your bid',
  'No. bids',
  'Highest bid',
];

const sellerHeadings = [
  'Item',
  'Name',
  'Time left',
  'Your price',
  'No. bids',
  'Highest bid',
];

const headerClassNames = [
  'auction-table__first-header space-left',
  'auction-table__second-header space-right space-left-second__header',
  'auction-table__third-header',
  'auction-table__fourth-header',
  'auction-table__fifth-header',
  'auction-table__sixth-header space-right',
];

const bodyClassNames = [
  'auction-table__first-body space-left',
  'auction-table__second-body',
  'auction-table__third-body',
  'auction-table__fourth-body',
  'auction-table__fifth-body',
  'auction-table__sixth-body',
];

const MyAccountPage = () => {
  const { pathname } = useLocation();
  const [selectedTab, setSelectedTab] = useState(
    pathname === bidsPath ? bidsPath : sellerPath
  );

  const navigate = useNavigate();

  const handleTabClick = (path) => {
    setSelectedTab(path);
    navigate(`/my-account${path}`);
  };

  return (
    <>
      <BreadCrumbs />
      <div className='wrapper'>
        <div className='my-account-page__tabs--and-btn'>
          <div className='my-account-page__tabs'>
            {myAccountTabs.map((tab, index) => (
              <div key={index}>
                <Link
                  to={tab.path}
                  className={selectedTab === tab.path ? 'selected-tab' : ''}
                  onClick={() => handleTabClick(tab.path)}
                >
                  <tab.icon /> {tab.title}
                </Link>
              </div>
            ))}
          </div>
          <Link to={sellerToSellItemPath}>
            <Button
              className={'my-account-page__btn'}
              SocialMediaIcon={AiOutlinePlus}
            >
              ADD ITEM
            </Button>
          </Link>
        </div>
        {(() => {
          switch (selectedTab) {
            case sellerPath:
              return (
                <SellerTab
                  sellerHeadings={sellerHeadings}
                  headerClassNames={headerClassNames}
                  bodyClassNames={bodyClassNames}
                />
              );
            case bidsPath:
              return (
                <BidsTab
                  bidHeadings={bidHeadings}
                  headerClassNames={headerClassNames}
                  bodyClassNames={bodyClassNames}
                />
              );
            default:
              return (
                <SellerTab
                  sellerHeadings={sellerHeadings}
                  headerClassNames={headerClassNames}
                  bodyClassNames={bodyClassNames}
                />
              );
          }
        })()}
      </div>
    </>
  );
};

export default MyAccountPage;
