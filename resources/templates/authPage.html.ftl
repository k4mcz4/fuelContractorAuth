<!DOCTYPE html>
 <html>
    <head>
        <title>Authorization</title>
        <#include "authPage.css">
        <link rel="stylesheet"  type="text/css" href="authPage.css" />

    </head>
    <body>
    <div id="conteiner">
        
            
        
        <div style="padding: 0%;" id="header">
            <h1 style="float: left; margin-left: 20px;">${user.name}</h1>
            <h2 style="float: right; margin-right: 30px;">${user.account}</h2>
        </div>
        <div id="content">
            <div id="navig">
                    <h2 style="text-align: center;">Navigation</h2>
                    <ul>
                        <li><a href="">Orders</a></li>
                        <li><a href="">Assets</a></li>
                        <li><a href="">Transactions</a></li>
                    </ul>
            </div>
            <div id="main">
                <img id="MiningDrone" src="https://imageserver.eveonline.com/Type/43701_64.png">
                <div id="column">
                <h4 id="text1" style=" margin-right: -200px;">Drones / Mining Drones /</h4>
                <h3 id="text2" style=" margin-right: 40px;">'Augmented' Ice Harvesting Drone</h3>
                <h4 id="text3" style=" margin-right: 40px; color: grey;">50 m3</h4>
                </div>
            </div>
        </div>
    </div>
    
    </body>


</html>