import React from 'react';
import './breadcrumbs.css';
import useBreadcrumbs from 'use-react-router-breadcrumbs';

const BreadCrumbs = ({ title }) => {
  const breadcrumbs = useBreadcrumbs();
  let lastElement =
    breadcrumbs[
      breadcrumbs.length - (title ? 2 : 1)
    ].breadcrumb.props.children.split(' ');
  let secondToLastElement =
    breadcrumbs[
      breadcrumbs.length - (title ? 3 : 2)
    ].breadcrumb.props.children.split(' ');

    const capitalize = (words) => {
        return words[0].toUpperCase();
    };

  lastElement = capitalize(lastElement);
  secondToLastElement = capitalize(secondToLastElement);

  return (
    <div className='breadcrumbs'>
      <div className='breadcrumbs__container'>
        <span>{title ? title : lastElement}</span>
        <div className='breadcrumbs__path'>
          <span>{secondToLastElement}&nbsp;</span>
          <p>/</p>
          <span>{lastElement}</span>
        </div>
      </div>
    </div>
  );
};

export default BreadCrumbs;
