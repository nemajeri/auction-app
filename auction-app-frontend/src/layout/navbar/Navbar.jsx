import React, { useState, useEffect, useContext, useRef } from 'react';
import { SocialMediaIcons } from '../../components/social-icons/SocialMediaIcons';
import './navbar.css';
import Logo from '../../assets/Logo';
import SearchField from '../../components/search-field/SearchField';
import NavbarLink from '../../components/nav-link/NavbarLink';
import navlinks from '../../data/navlinks.json';
import { useLocation, Link, useNavigate } from 'react-router-dom';
import { AppContext } from '../../utils/AppContextProvider';
import { loginPath, registrationPath } from '../../utils/paths';
import { logoutUser } from '../../utils/api/authApi.js';
import { getNotifications } from '../../utils/api/notificationApi';
import { EMPTY_STRING } from '../../utils/constants';
import { ACTIONS } from '../../utils/appReducer';
import { toast } from 'react-toastify';
import NotificationsCenter from '../../components/notifications/NotificationsCenter';
import WebSocketService from '../../services/WebSocketService';

const Navbar = () => {
  const {
    dispatch,
    searchTerm,
    onSearchIconClick,
    onSearchTermChange,
    suggestion,
    activeCategory,
    user,
  } = useContext(AppContext);
  const [activeLink, setActiveLink] = useState('home');
  const [notifications, setNotifications] = useState([]);
  const { pathname } = useLocation();
  const navigate = useNavigate();
  console.log('notifications: ', notifications);

  useEffect(() => {
    setActiveLink(pathname.replace('/', '') || 'home');
  }, [pathname]);

  const webSocketServiceRef = useRef(null);

  useEffect(() => {
    if (user) {
      let subscription;
        const fetchInitialNotifications = async () => {
            try {
                const response = await getNotifications(user?.id);
                console.log('response: ', response);
                setNotifications(response.data);
            } catch (error) {
                console.error('An error occurred while fetching notifications:', error);
            }
        }

        fetchInitialNotifications().then(() => {
            
            webSocketServiceRef.current = new WebSocketService();

            webSocketServiceRef.current.connect({}, () => {
                subscription = webSocketServiceRef.current.subscribe(
                    `/queue/notifications-${user.id}`,
                    (message) => {
                        const newNotification = JSON.parse(message.body);
                        console.log('newNotification: ', newNotification);
                        setNotifications((prevNotifications) => [
                            ...prevNotifications,
                            newNotification,
                        ]);
                    }
                );
                webSocketServiceRef.current.send('/app/get-notifications', {});
            });
        });

        return () => {
            if (subscription) {
                subscription.unsubscribe();
            }

            if (webSocketServiceRef.current) {
                webSocketServiceRef.current.disconnect();
            }
            setNotifications([]);
        };
    }
}, [user]);

  const handleLogout = async () => {
    try {
      await logoutUser();
      dispatch({ type: ACTIONS.SET_USER, payload: null });
      navigate('/login');
    } catch (error) {
      toast.error(error);
    }
  };

  const showSuggestion = () => {
    if (suggestion === 'No suggestion found' && searchTerm !== EMPTY_STRING) {
      return (
        <div className='navbar__suggestion--pop-up'>
          <div className='navbar__suggestion--pop-up_container not-found'>
            <p>{suggestion}</p>
          </div>
        </div>
      );
    } else if (suggestion !== EMPTY_STRING && searchTerm !== EMPTY_STRING) {
      return (
        <div className='navbar__suggestion--pop-up'>
          <div className='navbar__suggestion--pop-up_container'>
            <span>Did you mean?</span>
            <p
              onClick={(e) => {
                if (suggestion) {
                  onSearchIconClick(e, null, suggestion, navigate, pathname);
                  dispatch({
                    type: ACTIONS.SET_SEARCH_TERM,
                    payload: EMPTY_STRING,
                  });
                  dispatch({
                    type: ACTIONS.SET_SUGGESTION,
                    payload: EMPTY_STRING,
                  });
                } else {
                  dispatch({
                    type: ACTIONS.SET_SUGGESTION,
                    payload: EMPTY_STRING,
                  });
                }
              }}
            >
              {suggestion}
            </p>
          </div>
        </div>
      );
    } else {
      return null;
    }
  };

  return (
    <>
      <div className='navbar__black'>
        <div className='navbar__container'>
          <div>
            <SocialMediaIcons />
          </div>
          {user === null ? (
            <div className='navbar__account'>
              <Link to={loginPath}>Login</Link>
              <span>or</span>
              <Link to={registrationPath}>Create an Account</Link>
            </div>
          ) : (
            <div className='navbar__active-account'>
              {notifications?.length > 0 && (
                <NotificationsCenter notifications={notifications} webSocketService={webSocketServiceRef.current} setNotifications={setNotifications} />
              )}
              <p>
                Hi, {user.firstName} {user.lastName}
              </p>
              <p className='logout-link' onClick={handleLogout}>
                Log Out
              </p>
            </div>
          )}
        </div>
      </div>
      <div className='navbar__white'>
        <div className='navbar__container--white'>
          <Logo />
          {!(pathname.includes('login') || pathname.includes('register')) && (
            <div className='navbar__container--search_and-links'>
              <SearchField
                searchTerm={searchTerm}
                onSearchTermChange={onSearchTermChange}
                categoryId={activeCategory}
                onSearchIconClick={onSearchIconClick}
                pathname={pathname}
                navigate={navigate}
              />
              <div className='navbar__navigation'>
                {navlinks.map((link) => (
                  <NavbarLink
                    link={link}
                    key={link.key}
                    activeLink={activeLink}
                    onClick={setActiveLink}
                    user={user}
                  />
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
      {showSuggestion()}
    </>
  );
};

export default Navbar;
