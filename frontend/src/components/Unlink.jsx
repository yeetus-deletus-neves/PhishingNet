import { useAuthentication } from "./auth/AuthProvider";
import { defaultFetch } from "../utils/fetch";
import { MsalInterface } from "../scripts/msal";
import { setStoredInfo } from "../scripts/localstorage";
import { useAlertContext } from "./Layout";

export function UnlinkPage(){

    const [userInfo, setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()

    if(userInfo.email){
        return(
            <div className="container">
                <div id="container" className="center">
                    <h2 className="center">Do you want to unlink the email {userInfo.email} form your account?</h2>
                    <button type="button" onClick={async ()=>{
                        try{
                            const tokenRsp = await defaultFetch(
                                'http://localhost:8080/user/unlink',
                                "DELETE",
                                {
                                    'Content-Type': 'application/json',
                                    'Authorization': `Bearer ${userInfo.token.token}`
                                }
                            )
                            userInfo.email = null
                            const newUserInfo = userInfo
                            setUserInfo(null)
                            setUserInfo(newUserInfo)
                            setStoredInfo(userInfo)
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
                    <button type="button" onClick={()=>{
                        const msalAgent = new MsalInterface()
                        msalAgent.login()
                    }}>Link account</button>
                </div>
            </div>
        )
    }
}