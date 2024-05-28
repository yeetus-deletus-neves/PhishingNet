import { useAuthentication } from "../auth/authenticationProvider"
import { deleteStoredInfo } from "../utils/localstorage"
import { useAlertContext } from "./Layout"

export function NotLinked(){
    const [userInfo, setUserInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()

    return (
        <div>
            <h3>Hi {userInfo.username} your account is not linked</h3>
            <button type="button" onClick={()=>{
                window.open('http://localhost:3000/','_blank')
            }}>Link account</button>
        </div>
    )
}