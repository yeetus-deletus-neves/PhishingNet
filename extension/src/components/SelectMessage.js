import { useAuthentication } from "../auth/authenticationProvider"
import { deleteStoredInfo } from "../utils/localstorage"
import { useAlertContext } from "./Layout"

export function SelectMessage(){
    const [userInfo, setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()

    return(
        <div>
            <h3>Select a message</h3>
            <button type="button" onClick={()=>{
                deleteStoredInfo()
                setUserInfo(null)
                setAlert({alert: "warning", message: "user logged out"})
            }}>Logout</button>
        </div>
    )
}