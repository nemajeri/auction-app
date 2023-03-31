import React from 'react';

export const useGridView = (Component) => {
  return (props) => (
    <div className='grid-view'>
      <Component {...props} />
    </div>
  );
};
