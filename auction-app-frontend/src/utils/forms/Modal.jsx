import React from 'react';
import { Link } from 'react-router-dom';

const Modal = ({ showModal, closePath, children }) => {
  if (!showModal) {
    return null;
  }

  return (
    <div className='modal'>
      <div className='modal-content'>
        <Link to={closePath}>
          <button className='close-button'>X</button>
        </Link>
        {children}
      </div>
    </div>
  );
};

export default Modal;
