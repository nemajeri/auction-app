import React from 'react';
import './breadcrumbs.css';
import useBreadcrumbs from 'use-react-router-breadcrumbs';

const BreadCrumbs = ({ title }) => {
  const breadcrumbs = useBreadcrumbs();
  let lastElement =
    breadcrumbs[
      breadcrumbs.length - (title ? 2 : 1)
    ].breadcrumb.props.children;
  let secondToLastElement =
    breadcrumbs[
      breadcrumbs.length - (title ? 3 : 2)
    ].breadcrumb.props.children;

  return (
    <div className='breadcrumbs'>
      <div className='breadcrumbs__container'>
        <span>{title ? title : lastElement.toUpperCase()}</span>
        <div className='breadcrumbs__path'>
          <span>{secondToLastElement.toUpperCase()}&nbsp;</span>
          <p>/</p>
          <span>{lastElement.toUpperCase()}</span>
        </div>
      </div>
    </div>
  );
};

export default BreadCrumbs;
