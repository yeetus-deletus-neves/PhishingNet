import {useRouteError} from "react-router-dom"
import React from "react";
import logo from "../../../static/icons/border-48.png"

export default function ErrorPage(){
    const error = useRouteError()
    return (
        <div className="error-page">
            <h1 className='error-title'>Oops!</h1>
            <img className="error-img" src={logo} alt='Error'/>
            <p>Sorry, an unexpected error has occurred.</p>
            <p className='error-info'>
                <i>{error.status}: {error.statusText}</i>
            </p>
        </div>
    );
}