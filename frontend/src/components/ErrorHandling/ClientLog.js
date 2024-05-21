import {Alert,AlertTitle} from "@mui/material"
import React from "react"

export function ClientLog(props){
    const type = props.context.alert

    return(
        <div className='alert'>
            <Alert severity={type} onClose={props.onClose}>
                <AlertTitle> {type.charAt(0).toUpperCase() + type.slice(1)} </AlertTitle>
                {props.context.message}
            </Alert>
        </div>
    )
}