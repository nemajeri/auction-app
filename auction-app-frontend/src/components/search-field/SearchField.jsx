import React,{ useState} from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import './search-field.css';

const SearchField = () => {
  const[ searchTerm, setSearchTerm ] = useState('');

  const handleSearchTermChange = (event) => {
    setSearchTerm(event.target.value);
  }

  const handleSendingSearchRequest = () => {
    //method to send post request
    console.log('Send post request')
  }

  return (
    <div className='search'>
      <input type='text' value={searchTerm} placeholder='Try enter: Shoes' onChange={handleSearchTermChange}/>
      <AiOutlineSearch className='search__icon' onClick={handleSendingSearchRequest}/>
    </div>
  );
};

export default SearchField;