import React, { useContext, useEffect } from 'react';
import { useMatches, useLocation } from 'react-router-dom';
import './breadcrumbs.css';
import { NavigationContext } from '../../utils/NavigationProvider';

const BreadCrumbs = () => {
  const { previousPath, handleNavigation } = useContext(NavigationContext);
  const location = useLocation();
  let matches = useMatches();
  let crumbs = matches
    .filter((match) => Boolean(match.handle?.crumb))
    .map((match) => match.handle.crumb(match.data));

    useEffect(() => {
      if (location.pathname !== previousPath) {
        handleNavigation(location);
      }
    }, [location, previousPath]);

  const formatPreviousPath = (previousPath) => {
    const formattedPath = previousPath.replace(/-|\//g, ' ').toUpperCase();
    return formattedPath;
  };

  return (
    <div className='breadcrumbs'>
      <div className='breadcrumbs__container'>
        <span>{crumbs[0].props.children.toUpperCase()}</span>
        <div className='breadcrumbs__path'>
          <span>{formatPreviousPath(previousPath)}</span>
          <p>/</p>
          <span>{location.pathname.replace(/-|\//g, ' ').toUpperCase()}</span>
        </div>
      </div>
    </div>
  );
};

export default BreadCrumbs;