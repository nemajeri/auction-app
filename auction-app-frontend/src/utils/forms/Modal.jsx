import React from 'react';

const Modal = ({ showModal, onClose, children }) => {
  if (!showModal) {
    return null;
  }

  return (
    <div className='modal'>
      <div className='modal-content'>
        <button className='close-button' onClick={onClose}>X</button>
        {children}
      </div>
    </div>
  );
};

export default Modal;
