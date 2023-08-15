import React, { useState, useRef, useEffect } from 'react';
import { IoNotifications } from 'react-icons/io5';
import Notification from './Notification';
import { ACTIONS } from '../../utils/appReducer';
import './notifications-center.css';

const NotificationsCenter = ({ notifications, dispatch }) => {
  const [isOpened, setIsOpened] = useState(false);
  const ref = useRef(null);

  const onNotificationBellClick = (state) => {
    setIsOpened(!state);
  };

  const removeNotificationFromState = (notificationId, productId) => {
    dispatch({
      type: ACTIONS.SET_NOTIFICATIONS,
      payload: { notificationId, productId }
    });
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleClickOutside = (e) => {
    if (ref.current && !ref.current.contains(e.target)) {
      setIsOpened(false);
    }
  };

  return (
    <div ref={ref} className='notification-center__container'>
      <div
        className='notification-bell__container'
        onClick={() => onNotificationBellClick(isOpened)}
      >
        <div className='red-dot'>
          <p>{notifications.length}</p>
        </div>
        <IoNotifications className='bell-icon' data-testid="notifications-bell" />
      </div>
      {isOpened && (
        <div>
          <div className={`notifications__container ${isOpened ? 'open' : ''}`}>
            {notifications.map((notification, index) => (
              <Notification
                key={index}
                {...notification}
                setIsOpened={setIsOpened}
                removeNotificationFromState={removeNotificationFromState}
              />
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default NotificationsCenter;
