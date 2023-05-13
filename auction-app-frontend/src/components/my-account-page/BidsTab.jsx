import React, { useContext, useEffect, useState } from 'react';
import { RiAuctionFill } from 'react-icons/ri';
import AuctionTable from '../auction-table/AuctionTable';
import CallToAction from '../CallToAction';
import { AppContext } from '../../utils/AppContextProvider';
import { getBidsForUser } from '../../utils/api/bidApi';
import { hoursDiff } from '../../utils/helperFunctions';
import { Link } from 'react-router-dom';
import Button from '../../utils/Button';
import { shopPagePathToProduct } from '../../utils/paths';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';

const BidsTab = ({ bidHeadings, headerClassNames, bodyClassNames }) => {
  const { user, loading, setLoading } = useContext(AppContext);
  const [bids, setBids] = useState([]);

  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        const response = await getBidsForUser(user.id);
        setBids(response.data);
        setLoading(false);
      } catch (error) {
        console.error(error);
      }
    })();
  }, []);

  const getButtonLabel = (bid) => {
    switch (true) {
      case bid.product.sold && bid.product.user.id === user.id:
        return 'SOLD';
      case bid.product.sold &&
        bid.product.user.id !== user.id &&
        bid.user.id === user.id:
        return 'BOUGHT';
      case hoursDiff(bid.product.endDate) === 0 &&
        bid.product.highestBid === bid.price:
        return 'BUY';
      default:
        return 'VIEW';
    }
  }

  return !loading ? (
    <>
      <AuctionTable headings={bidHeadings} headerClassNames={headerClassNames}>
        {bids.length !== 0 ? (
          bids.map((bid) => (
            <tr key={bid.id}>
              {bid.product.images.length > 0 && (
                <td className={bodyClassNames[0]}>
                  <img
                    src={bid.product.images[0]}
                    alt={bid.product.productName}
                  />
                </td>
              )}
              <td className={bodyClassNames[1]}>
                <span>{bid.product.productName}</span>
                <br /> <span>#{bid.product.id}</span>
              </td>
              <td className={bodyClassNames[2]}>
                {hoursDiff(bid.product.endDate)} h
              </td>
              <td className={bodyClassNames[3]}>$ {bid.price.toFixed(2)}</td>
              <td className={bodyClassNames[4]}>{bid.product.numberOfBids}</td>
              <td
                className={`${bodyClassNames[5]} ${
                  bid.product.highestBid === bid.price
                    ? 'text-green'
                    : 'text-blue'
                }`}
              >
                {bid.product.highestBid !== null
                  ? '$ ' + bid.product.highestBid.toFixed(2)
                  : 'No bids'}
              </td>
              <td>
                <Link to={shopPagePathToProduct.replace(':id', bid.product.id)}>
                  <Button className={'auction-table__button'}>
                    {getButtonLabel(bid)}
                  </Button>
                </Link>
              </td>
            </tr>
          ))
        ) : (
          <tr className='shared-style__call-to-action_position'>
            <td>
              <CallToAction
                icon={<RiAuctionFill className='shared-style--icon' />}
                text='You don’t have any bids and there are so many cool products available for sale.'
                buttonLabel='START BIDDING'
                buttonClassName='shared-style__btn'
              />
            </td>
          </tr>
        )}
      </AuctionTable>
    </>
  ) : (
    <LoadingSpinner />
  );
};

export default BidsTab;
