import React from 'react';
import { useDropzone } from 'react-dropzone';

const Dropzone = ({ onDrop, accept }) => {
  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept,
  });

  return (
    <div {...getRootProps()} className='dropzone'>
      <input {...getInputProps()} />
      {isDragActive ? (
        <p>Drop the files here ...</p>
      ) : (
        <div className='dropzone__text'>
          <h5>Upload photos</h5>
          <h6>or just drag and drop</h6>
          <p>(Add at least 3 photos)</p>
        </div>
      )}
    </div>
  );
};

export default Dropzone;
