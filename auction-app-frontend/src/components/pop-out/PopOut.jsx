import React from 'react';
import './pop-out.css';

const PopOut = ({ popOut }) => {
  return (
    <div
      className={`pop-out ${popOut.visible ? 'visible' : 'hidden'} ${
        popOut.type
      }`}
    >
      <p>{popOut.message}</p>
    </div>
  );
};

export default PopOut;
