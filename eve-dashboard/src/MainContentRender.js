import React from 'react';
import './MainContentRender.css'
import DashboardView from './DashboardView.js'
import MiningView from './MiningView.js'

function DashboardContent(){
    return(
        <DashboardView />
    )

}

function MiningContent(){
    return(
        <MiningView />
    )

}

function IndustryContent(){
    return(
        <div>
            Industry Content
        </div>
    )

}

function MarketContent(){
    return(
        <div>
            Market Content
        </div>
    )

}

function SecurityContent(){
    return(
        <div>
            Security Content
        </div>
    )

}

export default DashboardContent
export {MiningContent,IndustryContent,MarketContent,SecurityContent}