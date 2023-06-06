import React, { useContext, useEffect, useState } from 'react';
import { RiAuctionFill } from 'react-icons/ri';
import AuctionTable from '../auction-table/AuctionTable';
import CallToAction from '../CallToAction';
import { AppContext } from '../../utils/AppContextProvider';
import { getBidsForUser } from '../../utils/api/bidApi';
import { hoursDiff } from '../../utils/helperFunctions';
import { useNavigate, Link } from 'react-router-dom';
import Button from '../../utils/Button';
import {
  shopPagePathToProduct,
  shopPagePathToCategory,
} from '../../utils/paths';
import LoadingSpinner from '../../components/loading-spinner/LoadingSpinner';
import { BUTTON_LABELS } from '../../utils/constants';

const BidsTab = ({ bidHeadings, headerClassNames, bodyClassNames }) => {
  const { user } = useContext(AppContext);
  const [bids, setBids] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isImageLoading, setIsImageLoading] = useState(true);

  const navigate = useNavigate();

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
  }, [user.id]);

  const getButtonLabel = (bid) => {
    switch (true) {
      case bid.product.sold && bid.product.userId === user.id:
        return BUTTON_LABELS.SOLD;
      case bid.product.sold &&
        bid.product.userId !== user.id &&
        bid.userId === user.id:
        return BUTTON_LABELS.BOUGHT;
      case hoursDiff(bid.product.endDate) === 0 &&
        bid.product.highestBid === bid.price:
        return BUTTON_LABELS.BUY;
      default:
        return BUTTON_LABELS.VIEW;
    }
  };

  return !loading ? (
    <>
      <AuctionTable headings={bidHeadings} headerClassNames={headerClassNames}>
        {bids.length !== 0 ? (
          bids.map((bid) => (
            <tr key={bid.id}>
              {bid.product.images.length > 0 && (
                <td className={bodyClassNames[0]}>
                  {isImageLoading && <LoadingSpinner pageSpinner={false} />}
                  <img
                    src={bid.product.images[0]}
                    alt={bid.product.productName}
                    onLoad={() => setIsImageLoading(false)}
                    className={
                      isImageLoading
                        ? 'product-image-loading'
                        : 'product-image-loaded'
                    }
                  />
                </td>
              )}
              <td className={bodyClassNames[1]}>
                <span>{bid.product.productName}</span>{' '}
                <span>#{bid.product.id}</span>
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
                  onClick={(e) => navigate(shopPagePathToCategory.replace(':categoryId', '1'))}
                />
            </td>
          </tr>
        )}
      </AuctionTable>
    </>
  ) : (
    <LoadingSpinner pageSpinner={true}/>
  );
};

export default BidsTab;
