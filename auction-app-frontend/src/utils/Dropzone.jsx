import React from 'react';
import { useDropzone } from 'react-dropzone';

const Dropzone = ({ onDrop, files = [], maxFiles = 1, fileType = 'text/csv' }) => {
  const { getRootProps, getInputProps, isDragActive, fileRejections } =
    useDropzone({
      onDrop,
      maxFiles: maxFiles,
      accept: ['image/jpeg', 'image/png', 'image/jpg', 'text/csv'],
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
          {fileType === 'text/csv' ? (
            <>
            <h5>Click to upload CSV, TSV</h5>
            <p>Accepted files: {files?.length}</p>
            <ul>{fileRejectionItems}</ul>
            </>
          ) : (
            <>
              <h5>Upload files</h5>
              <h6>or just drag and drop</h6>
              <p>Accepted files: {files.length}</p>
              <p>Rejected files:</p>
              <ul>{fileRejectionItems}</ul>
            </>
          )}
        </div>
      )}
    </div>
  );
};

export default Dropzone;
