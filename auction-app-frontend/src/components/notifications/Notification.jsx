import React from 'react';
import { useNavigate } from 'react-router-dom';
import { shopPagePathToProduct } from '../../utils/paths';
import { formatDate  } from '../../utils/helperFunctions';

const Notification = ({
  id,
  productId,
  date,
  type,
  description,
  setIsOpened,
  removeNotificationFromState,
}) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(shopPagePathToProduct.replace(':id', productId), {
      state: { openModal: formattedType === 'AUCTION FINISHED' },
    });
    setIsOpened(false);
    removeNotificationFromState(id, productId);
  };

  const formattedType = type.replace('_', ' ');

  return (
    <div className='notification unread' onClick={handleClick}>
      <header className='notification-header'>
        <time>{formatDate(date)}</time>
        <span>{formattedType}</span>
      </header>
      <section className='notification-content'>
        <p>{description}</p>
      </section>
    </div>
  );
};

export default Notification;
