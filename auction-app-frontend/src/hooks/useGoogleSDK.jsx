import { useEffect } from 'react';

const useGoogleSDK = (onLoad) => {
  useEffect(() => {
    const loadGoogleSDK = async () => {
      if (document.getElementById('google-sdk')) return;

      const script = document.createElement('script');
      script.id = 'google-sdk';
      script.src = 'https://apis.google.com/js/platform.js';
      script.async = true;
      script.defer = true;
      document.body.appendChild(script);

      script.onload = () => {
        if (onLoad) onLoad();
      };
    };

    loadGoogleSDK();
  }, [onLoad]);
};

export default useGoogleSDK;
