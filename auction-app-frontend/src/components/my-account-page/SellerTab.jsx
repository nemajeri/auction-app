import { useState, useEffect, useContext } from 'react';
import { FiShoppingCart } from 'react-icons/fi';
import Tabs from '../tabs/Tabs';
import AuctionTable from '../auction-table/AuctionTable';
import CallToAction from '../CallToAction';
import { ACTIVE, SOLD } from '../../utils/constants';
import { hoursDiff } from '../../utils/helperFunctions';
import { getProductsForUser } from '../../utils/api/productsApi';
import { AppContext } from '../../utils/AppContextProvider';
import Button from '../../utils/Button';
import { shopPagePathToProduct } from '../../utils/paths';
import { Link } from 'react-router-dom';

const tabs = [
  { id: 'active', label: ACTIVE },
  { id: 'sold', label: SOLD },
];

const SellerTab = ({ sellerHeadings, headerClassNames, bodyClassNames }) => {
  const { user } = useContext(AppContext);
  const [selectedTab, setSelectedTab] = useState(tabs[0].id);
  const [products, setProducts] = useState([]);

  const handleTabClick = (id) => {
    setSelectedTab(id);
  };

  useEffect(() => {
    (async () => {
      try {
        const tab = tabs.find((tab) => tab.id === selectedTab);
        const response = await getProductsForUser(user.id, tab.label);
        setProducts(response.data);
      } catch (error) {
        console.error(error);
        setProducts([]);
      }
    })();
  }, [selectedTab]);

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
      <AuctionTable
        headings={sellerHeadings}
        headerClassNames={headerClassNames}
      >
        {products.length !== 0 ? (
          products.map((product) => (
            <tr key={product.id}>
              {product.images.length > 0 && (
                <td className={bodyClassNames[0]}>
                  <img src={product.images[0]} alt={product.productName} />
                </td>
              )}
              <td className={bodyClassNames[1]}>
                <span>{product.productName}</span>
                <br /> <span>#{product.id}</span>
              </td>
              <td className={bodyClassNames[2]}>
                {hoursDiff(product.endDate)} h
              </td>
              <td className={bodyClassNames[3]}>$ {product.startPrice}</td>
              <td className={bodyClassNames[4]}>{product.numberOfBids}</td>
              <td className={bodyClassNames[5]}>
                {product.highestBid !== null
                  ? '$ ' + product.highestBid
                  : 'No bids'}
              </td>
              <td>
              <Link to={shopPagePathToProduct.replace(':id', product.id)}>
                  <Button className={'auction-table__button'}>VIEW</Button>
                </Link>
              </td>
            </tr>
          ))
        ) : (
          <tr className='shared-style__call-to-action_position'>
            <td>
              <CallToAction
                icon={<FiShoppingCart className='shared-style--icon' />}
                text='You do not have any scheduled items for sale.'
                buttonLabel='START SELLING'
                buttonClassName='shared-style__btn'
              />
            </td>
          </tr>
        )}
      </AuctionTable>
    </>
  );
};

export default SellerTab;