import React from 'react';
import './tabs.css';

const Tabs = ({ selectedTab, handleTabClick, tabs }) => {
  return (
    <div className='tabs'>
      {tabs.map((tab) => (
        <div
          key={tab.id}
          className={`tab ${
            selectedTab === tab.id ? 'tab--selected' : ''
          }`}
          onClick={() => handleTabClick(tab.id)}
        >
          <h2>{tab.label}</h2>
        </div>
      ))}
    </div>
  );
};

export default Tabs;
