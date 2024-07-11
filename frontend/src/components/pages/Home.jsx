import React from "react";
import { MsalInterface } from "../../scripts/msal";
import { useAuthentication } from "../auth/AuthProvider";

export function HomePage(){
    const [userInfo,setUserInfo] = useAuthentication()
    console.log(userInfo)
    if(userInfo){

        if(userInfo.email){
            return (
                <div className="container">
                    <div id="container" className="center">
                        <h2 className="center">Olá {userInfo.username}!</h2>
                        <br></br>
                        <h3 className="center">A sua conta está atualmente vinculada ao email: {userInfo.email}</h3>
                        <br></br>
                        <h3 className="center">Verifique que entra na caixa de mensagens desta conta para a extensão funcionar</h3>
                    </div>
                </div>
            );
        }else{
            return (
                <div className="container">
                    <div id="container" className="center">
                        <h2 className="center">Olá {userInfo.username}!</h2>
                        <h3 className="center">Conta não vinculada</h3>
                        <button className="btn btn-dark" onClick={()=>{
                            const msalAgent = new MsalInterface()
                            msalAgent.login()
                        }
                        }> Vincular conta </button>
                    </div>
                </div>
            );
        }
    }else{
        return(
            <div className="container">
                <div id="container" className="center">
                    <h1 className="center">Phishing Net</h1>
                    <h3>Não está atualmente logado</h3>
                </div>
            </div>
        )
    }
}