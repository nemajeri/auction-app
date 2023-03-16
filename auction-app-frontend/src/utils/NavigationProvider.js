import React, { useState, createContext } from 'react';

export const NavigationContext = createContext();

export const NavigationProvider = ({ children }) => {
  const [previousPath, setPreviousPath] = useState('/');


  const handleNavigation = (location) => {
    setPreviousPath(location.pathname); 
  };

  console.log(previousPath)

  return (
    <NavigationContext.Provider value={{ previousPath, handleNavigation }}>
      {children}
    </NavigationContext.Provider>
  );
};
