import React, { useState, useRef, useEffect } from 'react';
import { IoNotifications } from 'react-icons/io5';
import Notification from './Notification';
import './notifications-center.css';

const NotificationsCenter = ({ notifications, setNotifications }) => {
  const [isOpened, setIsOpened] = useState(false);
  const ref = useRef(null);

  const onNotificationBellClick = (state) => {
    setIsOpened(!state);
  };

  const removeNotificationFromState = (notificationId, productId) => {
    setNotifications(
      notifications.filter(
        (notification) =>
          notification.id !== notificationId &&
          notification.productId !== productId
      )
    );
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
        <IoNotifications className='bell-icon' />
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
