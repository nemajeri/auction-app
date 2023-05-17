import React from 'react'

const RenderProgressDots = ({ step }) => {
    const totalSteps = 3;
    const dots = [];

    for (let i = 1; i <= totalSteps; i++) {
      dots.push(
        <div className='sell-page__progress-dot_circle' key={i}>
          <div
            className={`sell-page__progress-dot ${
              i <= step ? 'sell-page__progress-active_dot' : ''
            }`}
          />
        </div>
      );
    }

    return (
      <div className='sell-page__progress-container'>
        <div className='sell-page__progress-line' />
        {dots}
      </div>
    );
}

export default RenderProgressDots