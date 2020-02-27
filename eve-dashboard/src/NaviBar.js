import React from 'react';
import './NaviBar.css'

function NaviBar(){
    return(
        <div id="naviBar">
            <NaviOpenButton />
            <NaviFuncButton />
        </div>
    );
}

function NaviOpenButton(){
    return(
        <div id="naviOpenButton">
            OPEN
        </div>
    );
}

function NaviFuncButton(){
    return(
        <div class="naviButton">
            Test
        </div>
    );
}

export default NaviBar