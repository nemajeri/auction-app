import React from 'react';
import './aboutPage.css';

const AboutPage = () => {
  return (
    <div className='wrapper'>
      <h1>About Us</h1>
      <div className='content about-us__page--content'>
        <div className='about-page__text'>
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis
            consequat pretium turpis, in eleifend mi laoreet sed. Donec ipsum
            mauris, venenatis sit amet porttitor id, laoreet eu magna. In
            convallis diam volutpat libero tincidunt semper. Ut aliquet erat
            rutrum, venenatis lacus ut, ornare lectus. Quisque congue ex sit
            amet diam malesuada, eget laoreet quam molestie. In id elementum
            turpis. Curabitur quis tincidunt mauris. Lorem ipsum dolor sit amet,
            consectetur adipiscing elit. Duis consequat pretium turpis, in
            eleifend mi laoreet sed. Donec ipsum mauris, venenatis sit amet
            porttitor id, laoreet eu magna. In convallis diam volutpat libero
            tincidunt semper. Ut aliquet erat rutrum, venenatis lacus ut, ornare
            lectus. Quisque congue ex sit amet diam malesuada, eget laoreet quam
            molestie. In id elementum turpis. Curabitur quis tincidunt mauris.
          </p>
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis
            consequat pretium turpis, in eleifend mi laoreet sed. Donec ipsum
            mauris, venenatis sit amet porttitor id, laoreet eu magna. In
            convallis diam volutpat libero tincidunt semper. Ut aliquet erat
            rutrum, venenatis lacus ut, ornare lectus. Quisque congue ex sit
            amet diam malesuada, eget laoreet quam molestie. In id elementum
            turpis. Curabitur quis tincidunt mauris. Lorem ipsum dolor sit amet,
            consectetur adipiscing elit. Duis consequat pretium turpis, in
            eleifend mi laoreet sed. Donec ipsum mauris, venenatis sit amet
            porttitor id, laoreet eu magna. In convallis diam volutpat libero
            tincidunt semper. Ut aliquet erat rutrum, venenatis lacus ut, ornare
            lectus. Quisque congue ex sit amet diam malesuada, eget laoreet quam
            molestie. In id elementum turpis. Curabitur quis tincidunt mauris.
          </p>
        </div>
        <div className='about-page__main--image'>
          <img
            src='https://images.unsplash.com/photo-1678669701650-46851754bcef?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1471&q=80'
            alt='beach'
          />
          <div className='about-page__images'>
            <img
              src='https://images.unsplash.com/photo-1678802250711-c2a46f579cf3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80'
              alt='city'
            />
            <img
              src='https://images.unsplash.com/photo-1678816252761-e77f87def9d7?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80'
              alt='desert'
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default AboutPage;
