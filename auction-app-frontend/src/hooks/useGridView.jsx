import React from 'react';

export const useGridView = (Component) => {
  return (props) => (
    <div className={`grid-view${props.className ? ` ${props.className}` : ''}`}>
      <Component {...props} />
    </div>
  );
};