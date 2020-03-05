import React from 'react';
import './MiningView.css';

function MiningView(){
    return(
        <div>
            <MaterialDataStrip displayList={dummyMaterialList}/>
            <MaterialDataStrip displayList={dummyOreList}/>
        </div>
    )
}

function MaterialDataStrip(props){
    return(
        <div id="materialStripContainer">
            <div id="sectionName">{props.displayList.materialType}</div>
            <div id="materialHorizontalStrip">
                {
                    props.displayList.materials.map(
                        function(material){
                            return(<MaterialTile key={material.itemTypeId} material={material} />)
                        }
                    )
                }
            </div>
        </div>
    )
}

function MaterialTile(props){
    return(
        <div id="materialTile">
            <MaterialTileHead materialDetail={props.material}/>
            {
                props.material.stations.map(
                    function(stationMaterial){
                        return(<MaterialTileStation key={stationMaterial.stationId + props.material.itemTypeId} station={stationMaterial} />)
                    }
                )
            }
        </div>
    )
}

function MaterialTileHead(props){
    return(
        <div id="materialHeadContainer">
            <div className="tileContent" id="materialIcon">{props.materialDetail.itemTypeId}</div>
            <div className="tileContent" id="materialName">{props.materialDetail.itemName}</div>
            <div className="tileContent" id="materialQuantity">{props.materialDetail.overallQty}</div>
        </div>
    )
}

function MaterialTileStation(props){
    return(
        <div id="materialStationContainer">
            <div className="tileContent" id="stationLocation">{props.station.stationLocation}</div>
            <div className="tileContent" id="stationName">{props.station.stationName}</div>
            <div className="tileContent" id="materialStationQuantity">{props.station.stationQty}</div>
        </div>
    )
}



const dummyMaterialList = 
    {
        materialType: "Minerals",
        materials: [
            {
                itemTypeId: 34,
                itemName: "Tritanium",
                overallQty: 2345212,
                stations: [
                    {
                        stationId: 1235,
                        stationLocation: "Jita",
                        stationName: "ProductionStation",
                        stationQty: 2535
                    },
                    {
                        stationId: 1234,
                        stationLocation: "Pita",
                        stationName: "MiningStation",
                        stationQty: 34445535
                    },
                    {
                        stationId: 1236,
                        stationLocation: "Lita",
                        stationName: "SomewhereStation",
                        stationQty: 142535
                    }
                ]
            },

            {
                itemTypeId: 35,
                itemName: "Perite",
                overallQty: 58212,
                stations: [
                    {
                        stationId: 1235,
                        stationLocation: "Jita",
                        stationName: "ProductionStation",
                        stationQty: 3444
                    },
                    {
                        stationId: 1234,
                        stationLocation: "Pita",
                        stationName: "MiningStation",
                        stationQty: 445535
                    },
                    {
                        stationId: 1236,
                        stationLocation: "Lita",
                        stationName: "SomewhereStation",
                        stationQty: 2142535
                    }
                ]
            }
        ]
    }

    

const dummyOreList = 
    {
        materialType: "Ores",
        materials: [{

            itemTypeId: 77,
            itemName: "Veldspar",
            overallQty: 777212,
            stations: [
                {
                    stationId: 1235,
                    stationLocation: "Jita",
                    stationName: "ProductionStation",
                    stationQty: 588
                },
                {
                    stationId: 1234,
                    stationLocation: "Pita",
                    stationName: "MiningStation",
                    stationQty: 114477
                },
                {
                    stationId: 1236,
                    stationLocation: "Lita",
                    stationName: "SomewhereStation",
                    stationQty: 66398
                }
            ]
        },

        {
            itemTypeId: 78,
            itemName: "Omber",
            overallQty: 111877,
            stations: [
                {
                    stationId: 1235,
                    stationLocation: "Jita",
                    stationName: "ProductionStation",
                    stationQty: 1222
                },
                {
                    stationId: 1234,
                    stationLocation: "Pita",
                    stationName: "MiningStation",
                    stationQty: 879535
                },
                {
                    stationId: 1236,
                    stationLocation: "Lita",
                    stationName: "SomewhereStation",
                    stationQty: 887795
                }
            ]
        }
        ]
    }



export default MiningView