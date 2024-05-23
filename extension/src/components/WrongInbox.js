import { useAuthentication } from "../auth/authenticationProvider"
import { deleteStoredInfo } from "../utils/localstorage"
import { useAlertContext } from "./Layout"

export function WrongInbox({inboxMail}){
    const [userInfo, setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()

    return (
        <div>
            <h3>You should be in the inbox of the email {userInfo.email}</h3>
            {inboxMail ? <h3>Instead you are in the inbox of {inboxMail}</h3> : <h3>Currently not in an inbox</h3>}
            <button type="button" onClick={()=>{
                deleteStoredInfo()
                setUserInfo(null)
                setAlert({alert: "warning", message: "user logged out"})
            }}>Logout</button>
        </div>    
    )
}