import React from 'react';
import { useDropzone } from 'react-dropzone';

const Dropzone = ({ onDrop, images }) => {
  const {
    getRootProps,
    getInputProps,
    isDragActive,
    fileRejections,
  } = useDropzone({ 
    onDrop,
    maxFiles: 5,
  });

  const fileRejectionItems = fileRejections.map(({ file, errors }) => (
    <li key={file.path}>
      {file.path} - {errors.map((e) => e.message).join(', ')}
    </li>
  ));

  return (
    <div {...getRootProps()} className='dropzone'>
      <input {...getInputProps()} />
      {isDragActive ? (
        <p>Drop the files here ...</p>
      ) : (
        <div className='dropzone__text'>
          {images.length <= 5 ? (
            <>
              <h5>Upload photos</h5>
              <h6>or just drag and drop</h6>
              <p>Accepted files: {images.length}</p>
              <p>Rejected files:</p>
              <ul>{fileRejectionItems}</ul>
            </>
          ) : (
            <h5>Upload completed.</h5>
          )}
        </div>
      )}
    </div>
  );
};

export default Dropzone;
