import { useEffect } from 'react';

const useFacebookSDK = () => {
  useEffect(() => {
    const loadFacebookSDK = async () => {
      if (document.getElementById('facebook-jssdk')) return;

      const script = document.createElement('script');
      script.id = 'facebook-jssdk';
      script.src = '//connect.facebook.net/en_US/sdk.js';
      script.async = true;
      script.defer = true;
      document.body.appendChild(script);

      script.onload = () => {
        window.FB.init({
          appId: process.env.REACT_APP_FACEBOOK_APP_ID,
          cookie: true,
          xfbml: true,
          version: 'v16.0',
        });
      };
    };

    loadFacebookSDK();
  }, []);
};

export default useFacebookSDK;