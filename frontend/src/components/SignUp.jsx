import React from "react";
import {Input, InputAdornment, InputLabel} from "@mui/material";
import {AccountCircle} from "@mui/icons-material";
import LockIcon from "@mui/icons-material/LockPerson";
import { useNavigate } from "react-router";

export function SignUpPage(){
    const navigate = useNavigate();

    function handleSubmit(ev){
        ev.preventDefault();
        // do stuff to verify password
    }
    function handleSignUp(){
        navigate("/signUp")
    }

    return (
        <div className="login-form">
            <h1><b>Login</b></h1><hr/>
            <form onSubmit={handleSubmit}>
                <div>
                    <InputLabel htmlFor= "username" className="mui-theme">
                        Username
                    </InputLabel>
                    <Input 
                        className="mui-theme mui-input" 
                        id="username" 
                        name="username" 
                        required={true} 
                        startAdornment={
                            <InputAdornment position="start">
                                <AccountCircle className="mui-theme"/>
                            </InputAdornment>
                        }
                    />
                </div>
                <div>
                    <InputLabel htmlFor="password" className='mui-theme'>
                        Password
                    </InputLabel>
                    <Input
                        className='mui-theme mui-input'
                        id="password"
                        type="password"
                        required={true}
                        name="password"
                        startAdornment={
                            <InputAdornment position="start">
                                <LockIcon className='mui-theme'/>
                            </InputAdornment>
                        }
                    />
                </div>
                <div>
                    <button className="styled-button" type="submit">Submit</button>
                    <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={handleSignUp}>Sign up here</a></div>
                </div>
            </form>
        </div>
    );
}