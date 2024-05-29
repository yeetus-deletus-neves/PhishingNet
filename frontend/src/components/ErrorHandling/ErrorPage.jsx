import {Link, useRouteError} from "react-router-dom"
import React from "react";

export function ErrorPage(){
    const error = useRouteError()
    return (
        <div className="container">
            <div className="error-page center">
                <img class="" src="./icons/oops.png"/>
                <p className='error-info'>
                    <h3> Error {error.status}: {error.statusText}</h3>
                </p>
                <Link className="btn btn-dark" to="/">Back to homepage</Link>
            </div>
        </div>
    );
}