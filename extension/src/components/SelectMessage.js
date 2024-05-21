import { useAuthentication } from "../utils/auth"
import { About } from "./About"
import { deleteStoredInfo } from "../utils/localstorage"

export function SelectMessage(){
    const [userInfo, setUserInfo] = useAuthentication()

    return(
        <main>
            <About/>
            <h3>Select a message</h3>
            <button type="button" onClick={()=>{
                deleteStoredInfo()
                setUserInfo(null)
            }}>Logout</button>
        </main>
    )
}