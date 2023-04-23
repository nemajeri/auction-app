import React from 'react';
import './pop-out.css';

const PopOut = ({ popOut }) => {
  return (
    <div className={`pop-out__container ${popOut.type}`}>
      <div className='wrapper pop-out__wrapper'>
        <div className={`pop-out ${popOut.visible ? 'visible' : 'hidden'}`}>
          <p>{popOut.message}</p>
        </div>
      </div>
    </div>
  );
};

export default PopOut;
