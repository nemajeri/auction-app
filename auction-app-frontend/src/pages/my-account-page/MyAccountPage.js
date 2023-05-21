import React, { useState, useContext } from 'react';
import './myAccountPage.css';
import BreadCrumbs from '../../components/breadcrumbs/Breadcrumbs';
import { myAccountTabs } from '../../utils/constants';
import Button from '../../utils/Button';
import { sellerPath, bidsPath, sellerToAddItemPath } from '../../utils/paths';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import SellerTab from '../../components/my-account-page/SellerTab';
import BidsTab from '../../components/my-account-page/BidsTab';
import { AiOutlinePlus } from 'react-icons/ai';
import { AppContext } from '../../utils/AppContextProvider';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import { usePageLoading } from '../../hooks/usePageLoading';
import { headerClassNames, bodyClassNames } from '../../utils/styles';
import {sellerHeadings, bidHeadings} from '../../data/headings';

const MyAccountPage = () => {
  const { pathname } = useLocation();
  const [selectedTab, setSelectedTab] = useState(
    pathname === bidsPath ? bidsPath : sellerPath
  );
  const { initialLoading } =  useContext(AppContext);

  const navigate = useNavigate();

  usePageLoading(800);

  const handleTabClick = (path) => {
    setSelectedTab(path);
    navigate(`/my-account${path}`);
  };

  if (initialLoading) {
    return <LoadingSpinner pageSpinner={true} />;
  }

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
          <Link to={sellerToAddItemPath}>
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
