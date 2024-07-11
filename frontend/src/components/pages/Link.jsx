import { setStoredInfo } from "../../scripts/localstorage";
import { defaultFetch } from "../../utils/fetch";
import { useAuthentication } from "../auth/AuthProvider";
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
                        const newInfo = {userId:userInfo.userId,email:rsp.email,token:userInfo.token,username:userInfo.username}
                        setUserInfo(newInfo)
                        setStoredInfo(newInfo)
                        setLinked(true)
                    }else{
                        setError(true)
                    }
                })
            }catch(error){
                if(error.details){
                    setAlert({alert: "error", message: `${error.details}`})
                }else{
                    setAlert({alert: "error", message: "Erro interno de servidor"})
                }
            }
        }
    }
    if(isLinked){
        return (
            <div className="container">
                <div id="container" className="center">
                    <h3 className="center">Vínculo com {userInfo.email} foi bem-sucedido</h3>
                </div>
            </div>
        )
    }else{
        if(isError){
            return (
                <div className="container"> 
                    <div id="container" className="center">
                        <h1 className="center">Vínculo mal-sucedido</h1>
                    </div>
                </div>    
            )    
        }else{
            return (
                <div className="container">
                    <div id="container" className="center">
                        <h1 className="center">Aguardando vinculação</h1>
                    </div>
                </div>
            )
        }
    }
}
