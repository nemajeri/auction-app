export const selectStyles = {
    position: 'relative',
  };
  
  export const selectArrowStyles = {
    content: '',
    position: 'absolute',
    right: '1rem',
    top: '50%',
    transform: 'translateY(-50%)',
    backgroundImage: `url(${process.env.REACT_APP_HOME_URL}/images/down-arrow.png)`,
    backgroundRepeat: 'no-repeat',
    backgroundPosition: 'center',
    backgroundSize: '1rem',
    width: '1rem',
    height: '1rem',
    pointerEvents: 'none',
  };
  
  export const customSelectStyles = {
    appearance: 'none',
    background: 'transparent',
    paddingRight: '2rem',
  };