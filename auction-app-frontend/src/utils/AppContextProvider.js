import React, { useState, createContext } from 'react';

export const AppContext = createContext();

export const AppContextProvider = ({ children }) => {
  const [previousPath, setPreviousPath] = useState('/');
  const [searchTerm, setSearchTerm] = useState('');

  console.log('Search term ', searchTerm)

  const handleNavigation = (location) => {
    setPreviousPath(location.pathname);
  };

  const onSearchTermChange = (event) => {
    setSearchTerm(event.target.value);
  };


  return (
    <AppContext.Provider
      value={{
        previousPath,
        handleNavigation,
        searchTerm,
        onSearchTermChange,
      }}
    >
      {children}
    </AppContext.Provider>
  );
};
