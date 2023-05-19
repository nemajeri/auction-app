import React from 'react';
import './loadingSpinner.css';

const LoadingSpinner = ({ pageSpinner }) => {
  return (
    <div className={`loading-spinner__container ${pageSpinner ? 'page-spinner' : ''}`}>
      <div className="loading-spinner"></div>
    </div>
  );
};

export default LoadingSpinner;