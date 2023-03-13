import React from 'react';
import './footer.css';
import foolinks from '../../data/foolinks.json';
import FooterItem from '../../components/footer-item/FooterItem';

const Footer = () => {
  const renderLinks = () =>
    foolinks[0].links.map((link) => (
      <div key={link.title}>
        <a href={link.url}>{link.title}</a>
      </div>
    ));

  const renderData = () =>
    foolinks[1].data.map((item) => (
      <div key={item.key}>
        <FooterItem item={item} />
      </div>
    ));

  return (
    <div className='footer'>
      <div className='footer__container'>
        {Object.entries(foolinks).map(([key, value]) => (
          <div key={key}>
            <div>
              <h4>{value.heading}</h4>
              {value.links && renderLinks()}
            </div>
            <div>{value.data && renderData()}</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Footer;
