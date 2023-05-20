import React, { useState } from 'react';
import LoadingSpinner from '../loading-spinner/LoadingSpinner';

const Image = ({ src, alt }) => {
  const [isLoading, setIsLoading] = useState(true);

  return (
    <div className="image-container">
      {isLoading && (
        <div className="image-loading">
          <LoadingSpinner />
        </div>
      )}
      <img
        className={`image ${isLoading ? 'image-loading' : 'image-loaded'}`}
        src={src}
        alt={alt}
        onLoad={() => setIsLoading(false)}
      />
    </div>
  );
};

export default Image;