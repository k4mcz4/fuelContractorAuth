import React from 'react';
import './DashboardView.css';

function DashboardView(){
    return(
        <DashboardStrip />
    );
}

function DashboardStrip(){
    const materialList = [
        {itemId: 11, name : "Tritanium", amount : "13,000,000", index : "+1%"},
        {itemId: 12, name : "Perite", amount : "231,000,000", index : "+1%"},
        {itemId: 13, name : "Unobtainium", amount : "11,000,000", index : "+1%"},
        {itemId: 14, name : "Uranium", amount : "1,000,000", index : "+1%"},
        {itemId: 15, name : "Somtium", amount : "1,000,000", index : "+1%"},
        {itemId: 16, name : "Rareitanium", amount : "1,000,000", index : "+1%"},
        {itemId: 17, name : "Dipotanium", amount : "1,000,000", index : "+1%"},
        {itemId: 18, name : "Alotium", amount : "1,000,000", index : "+1%"}
    ]

    return(
        <div>
            <DashboardChartTileContainer materials={materialList} />
            <DashboardChartTileContainer materials={materialList} />
            <DashboardChartTileContainer materials={materialList} />
        </div>
    )
}

function DashboardChartTileContainer(props){

    return(
        <div id="tileContainerWrapper">
            <div id="dashboardTileContainer">
                {props.materials.map(
                    function(material){
                        return (
                            <DashboardTile key={material.itemId} material={material} />
                        )
                    }
                )}
            </div>
        </div>
    )
}

function DashboardTile(props){
    return(
        <div className="dashboardTile">
            <div id="tileName">{props.material.name}</div>
            <div id="tileWrapper">
                <div className="materialContent" id="materialAmount">
                    {props.material.amount}
                </div>
                <div className="materialContent" id="materialIndex">
                    {props.material.index}
                </div>
            </div>

        </div>
    )

}

export default DashboardView