import React from 'react';
import './tabs.css';

const Tabs = ({ selectedTab, handleTabClick, tabs, containerClassName, tabClassName, selectedTabClassName }) => {
  return (
    <div className={`${containerClassName}`} >
      {tabs.map((tab) => (
        <div
          key={tab.id}
          className={`${tabClassName} ${
            selectedTab === tab.id ? selectedTabClassName : ''
          }`}
          onClick={() => handleTabClick(tab.id)}
        >
          <h2>{tab.label}</h2>
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