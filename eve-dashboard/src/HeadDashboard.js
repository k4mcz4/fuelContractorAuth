import React from 'react';
import './HeadDashboard.css'

function HeadMainContainer(){
    return(
        <div id="headMainContainer">
            <HeadUserGreet />
            <HeadUserCorporation />
            <HeadUserBalance />
        </div>
    );
}

function HeadUserGreet(){
    return(
        <div id="headUserGreet">Greetings USER_NAME</div>
    );
}

function HeadUserCorporation(){
    return(
        <div id="headUserCorp">CORP_NAME</div>
    );
}

function HeadUserBalance(){
    return(
        <div id="headUserBalance">ACCOUNT_BALANCE ISK</div>
    );
}


export default HeadMainContainer