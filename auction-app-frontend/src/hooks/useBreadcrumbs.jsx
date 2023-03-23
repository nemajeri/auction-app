import React from 'react';
import Breadcrumbs from '../components/breadcrumbs/Breadcrumbs';

export const useBreadcrumbs = (Component) => {
  return (props) => (
    <>
      <Breadcrumbs />
      <Component {...props} />
    </>
  );
};