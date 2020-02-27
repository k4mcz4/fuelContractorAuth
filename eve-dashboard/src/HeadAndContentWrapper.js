import React from 'react';
import './HeadAndContentWrapper.css'
import HeadMainContainer from './HeadDashboard.js'
import MainContent from './MainContent.js'

function HeadAndContentWrapper(){
    return(
        <div id="headAndContentWrapper">
            <HeadMainContainer />
            <MainContent />
        </div>
    );
}


export default HeadAndContentWrapper