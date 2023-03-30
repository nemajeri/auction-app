import React from 'react';

const NoImagePlaceholder = () => {
  return (
    <div className='no-image-icon'>
      <svg
        width='50px'
        height='50px'
        viewBox='0 0 14 14'
        role='img'
        focusable='false'
        aria-hidden='true'
        xmlns='http://www.w3.org/2000/svg'
      >
        <g transform='translate(.14285704 .14285704) scale(.28571)'>
          <path fill='#8367d8' d='M40 13v32H8V3h22z' />
          <path fill='#c4bfd6' d='M38.5 14H29V4.5z' />
          <path fill='#e4e5ec' d='M21 23l-7 10h14z' />
          <path fill='#e4e5ec' d='M28 26.4L23 33h10z' />
          <circle cx='31.5' cy='24.5' r='1.5' fill='#e4e5ec' />
        </g>
      </svg>
    </div>
  );
};

export default NoImagePlaceholder;
