import React from 'react';
import './NaviBar.css'
import DashboardContent from './MainContentRender.js'
import {MiningContent,IndustryContent,MarketContent,SecurityContent} from './MainContentRender.js'
import ReactDOM from 'react-dom';

function NaviBar(){
    return(
        <div id="naviBar">
            <NaviOpenButton />
            <NaviDashboardButton />
            <NaviMiningButton />
            <NaviIndustryButton />
            <NaviMarketButton />
            <NaviSecurityButton />
        </div>
    );
}

function NaviOpenButton(){
    return(
        <div id="naviOpenButton">
            &lt;
        </div>
    );
}

function NaviDashboardButton(){
    return(
        <div className="naviButton" id="dashboard" onClick={RenderDashboard}>
            Dashboard
        </div>
    );
}

function NaviMiningButton(){
    return(
        <div className="naviButton" id="mining" onClick={RenderMining}>
            Mining
        </div>
    );
}

function NaviIndustryButton(){
    return(
        <div className="naviButton" id="industry" onClick={RenderIndustry}>
            Industry
        </div>
    );
}

function NaviMarketButton(){
    return(
        <div className="naviButton" id="market" onClick={RenderMarket}>
            Market
        </div>
    );
}

function NaviSecurityButton(){
    return(
        <div className="naviButton" id="security" onClick={RenderSecurity}>
            Security
        </div>
    );
}

//Render Content

//Dashboard
function RenderDashboard(){
    ResetButtonColors()
    document.getElementById("dashboard").style.backgroundColor = "rgb(201, 201, 201)"
    document.getElementById("dashboard").style.color = "black"
    ReactDOM.render(<DashboardContent />,document.getElementById('mainContent'))
}

//Mining
function RenderMining(){
    ResetButtonColors()
    document.getElementById("mining").style.backgroundColor = "rgb(143, 143, 99)"
    document.getElementById("mining").style.color = "black"
    ReactDOM.render(<MiningContent />,document.getElementById('mainContent'))
}

//Industry
function RenderIndustry(){
    ResetButtonColors()
    document.getElementById("industry").style.backgroundColor = "rgb(180, 180, 52)"
    document.getElementById("industry").style.color = "black"
    ReactDOM.render(<IndustryContent />,document.getElementById('mainContent'))
}

//Market
function RenderMarket(){
    ResetButtonColors()
    document.getElementById("market").style.backgroundColor = "rgb(101, 202, 5)"
    document.getElementById("market").style.color = "black"
    ReactDOM.render(<MarketContent />,document.getElementById('mainContent'))
}

//Security
function RenderSecurity(){
    ResetButtonColors()
    document.getElementById("security").style.backgroundColor = "rgb(0, 152, 253)"
    document.getElementById("security").style.color = "black"
    ReactDOM.render(<SecurityContent />,document.getElementById('mainContent'))
}


//Function used to reset Button states
function ResetButtonColors(){
    var buttons = document.getElementsByClassName("naviButton");
    var len =  buttons.length;

    for(var i=0 ; i<len; i++){
        buttons[i].style.backgroundColor= "";
        buttons[i].style.color = "silver";
    }
}

export default NaviBar