import React from 'react';
import classNames from 'classnames';
import './tabs.css';

const Tabs = ({
  selectedTab,
  handleTabClick,
  tabs,
  containerClassName,
  tabClassName,
  selectedTabClassName,
  disableClick,
  labelClassName,
}) => {
  return (
    <div className={containerClassName}>
      {tabs.map((tab) => (
        <div
          key={tab.id}
          className={classNames(tabClassName, {
            [selectedTabClassName]: selectedTab === tab.id,
          })}
          onClick={disableClick ? undefined : () => handleTabClick(tab.id)}
        >
          <h2 className={labelClassName}>{tab.label}</h2>
        </div>
      ))}
    </div>
  );
};

Tabs.defaultProps = {
  containerClassName: 'tabs',
  tabClassName: 'tab',
  selectedTabClassName: 'tab--selected',
};

export default Tabs;
