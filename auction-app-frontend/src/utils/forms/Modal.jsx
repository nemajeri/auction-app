import React from 'react';
import { Link } from 'react-router-dom';

const Modal = ({ showModal, successPath }) => {
  if (!showModal) {
    return null;
  }

  return (
    <div className="modal">
      <div className="modal-content">
        <h2>Product created successfully</h2>
        <Link to={successPath}><button>Close</button></Link>
      </div>
    </div>
  );
};

export default Modal;
