import {useRouteError} from "react-router-dom"
import React from "react";

export function ErrorPage(){
    const error = useRouteError()
    return (
        <div className="error-page">
            <h1 className='error-title'>Oops!</h1>
            <p>Sorry, an unexpected error has occurred.</p>
            <p className='error-info'>
                <i>{error.status}: {error.statusText}</i>
            </p>
        </div>
    );
}