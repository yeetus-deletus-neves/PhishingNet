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
                    <h2 className="center">Do you want to unlink the email {userInfo.email} form your account?</h2>
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
                            setAlert({alert: "error", message: `${error.details}`})
                        }
                    }
                }>
                    Confirm unlink
                    </button>
                </div>
            </div>
        )
    }else{
        return(
            <div className="container">
                <div id="container" className="center">
                    <h2 className="center">Unlinking was successfull</h2>
                    <h3 className="center">Want to link a new account? Click bellow</h3>
                    <button className="btn btn-dark" onClick={()=>{
                        const msalAgent = new MsalInterface()
                        msalAgent.login()
                    }}>Link account</button>
                </div>
            </div>
        )
    }
}