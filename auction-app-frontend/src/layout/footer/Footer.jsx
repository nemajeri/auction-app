import React from 'react';
import './footer.css';
import foolinks from '../../data/foolinks.json';
import { SocialMediaIcons } from '../../components/social-icons/SocialMediaIcons';

const Footer = () => {
  return (
    <div className='footer'>
      <div className='footer__container'>
        <div>
        <h4>{foolinks[0]?.heading}</h4>
          {foolinks[0]?.links.map((link) => (
            <div key={link.title}>
              <a href={link.url}>{link.title}</a>
            </div>
          ))}
        </div>
        <div>
          <h4>{foolinks[1]?.heading}</h4>
          {foolinks[1]?.data.map((item) => {
            if (item.key === 'component') {
              return <SocialMediaIcons key={item.key} />;
            } else {
              return (
                <div key={item.key}>
                  <p>{item.contact}</p>
                </div>
              );
            }
          })}
        </div>
      </div>
    </div>
  );
};

export default Footer;
