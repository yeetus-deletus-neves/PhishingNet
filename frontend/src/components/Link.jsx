import { setStoredInfo } from "../scripts/localstorage";
import { defaultFetch } from "../utils/fetch";
import { useAuthentication } from "./auth/AuthProvider";
import { useState } from "react";
import { useAlertContext } from "./Layout";

export function LinkPage(){
    const [userInfo,setUserInfo] = useAuthentication()
    const [isLinked,setLinked] = useState(userInfo.email)
    const [isError,setError] = useState(false)
    const [alert, setAlert] = useAlertContext()

    const searchParams = new URLSearchParams(window.location.search)
    const azureCode = searchParams.get('code')

    if(!userInfo.email){
        if(azureCode){
            try{
                defaultFetch(
                    'http://localhost:8080/user/link',
                    "POST",
                    {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${userInfo.token.token}`
                    },
                    {
                        "token": azureCode
                    }
                ).then( rsp => {
                    console.log(rsp)
                    if(rsp.email){
                        userInfo.email = rsp.email
                        setLinked(true)
                        setUserInfo(userInfo)
                        setStoredInfo(userInfo)
                    }else{
                        setError(true)
                    }
                })
            }catch(error){
                setAlert({alert: "error", message: `${error.details}`})
            }
        }
    }
    if(isLinked){
        return (
            <div id="container" className="center">
                <h3 className="center">Link with {userInfo.email} was successfull</h3>
            </div>
        )
    }else{
        if(isError){
            return (
                <div id="container" className="center">
                    <h1 className="center">Link was not successfull</h1>
                </div>
            )    
        }else{
            return (
                <div id="container" className="center">
                    <h1 className="center">Waiting for linking</h1>
                </div>
            )
        }
    }
}
