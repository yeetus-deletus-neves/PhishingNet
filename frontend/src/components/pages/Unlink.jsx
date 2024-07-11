import { useAuthentication } from "../auth/AuthProvider";
import { defaultFetch } from "../../utils/fetch";
import { MsalInterface } from "../../scripts/msal";
import { setStoredInfo, takeEmailFromStoredInfo } from "../../scripts/localstorage";
import { useAlertContext } from "./Layout";

export function UnlinkPage(){

    const [userInfo, setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()

    if(userInfo.email){
        return(
            <div className="container">
                <div id="container" className="center">
                    <h2 className="center">Quer desvincular {userInfo.email} da sua conta?</h2>
                    <button className="btn btn-dark" onClick={async ()=>{
                        try{
                            const tokenRsp = await defaultFetch(
                                'http://localhost:8080/user/unlink',
                                "DELETE",
                                {
                                    'Content-Type': 'application/json',
                                    'Authorization': `Bearer ${userInfo.token.token}`
                                }
                            )
                            const newInfo = {userId:userInfo.userId,email:null,token:userInfo.token,username:userInfo.username}
                            setUserInfo(newInfo)
                            setStoredInfo(newInfo)
                        }catch(error){
                            if(error.details){
                                setAlert({alert: "error", message: `${error.details}`})
                            }else{
                                setAlert({alert: "error", message: "Erro interno de servidor"})
                            }
                        }
                    }
                }>
                    Confirmar
                    </button>
                </div>
            </div>
        )
    }else{
        return(
            <div className="container">
                <div id="container" className="center">
                    <h2 className="center">Desvinculação bem-sucedida</h2>
                    <h3 className="center">Quer vincular uma conta nova? Clique embaixo</h3>
                    <button className="btn btn-dark" onClick={()=>{
                        const msalAgent = new MsalInterface()
                        msalAgent.login()
                    }}>Vincular conta</button>
                </div>
            </div>
        )
    }
}