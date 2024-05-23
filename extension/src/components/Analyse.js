import { useAuthentication } from "../auth/authenticationProvider";
import { defaultFetch } from "../utils/fetch";
import { deleteStoredInfo } from "../utils/localstorage";
import { useAlertContext } from "./Layout";

export function Analyse({url}){
    const [userInfo,setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()

    return (
        <div>
            <button type="button" onClick={ async ()=>{
                const startIndex = url.indexOf("id/");

                // Extract the substring
                const conversationID = url.substring(startIndex+3);
                try{
                    const analyseRsp = await defaultFetch(
                        'http://localhost:8080/analyse',
                        "POST",
                        {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${userInfo.token.token}`,
                        },
                        {
                            "content":`${conversationID}`
                        }
                    )
                    console.log(analyseRsp)
                }catch(error){
                    setAlert({alert: "error", message: `${error.details}`})
                }
            }}>Analyse Content</button>
            <button type="button" onClick={()=>{
                deleteStoredInfo()
                setUserInfo(null)
                setAlert({alert: "warning", message: "user logged out"})
            }}>Logout</button>
        </div>
    )
}