import { useAuthentication } from "../utils/auth"
import { About } from "./About"
import { deleteStoredInfo } from "../utils/localstorage"

export function NotLinked(){
    const [userInfo, setUserInfo] = useAuthentication()

    return (
        <main>
            <About/>
            <h3>Hi {userInfo.username} your account is not linked</h3>
            <button type="button" onClick={()=>{
                window.open('http://localhost:3000/','_blank')
            }}>Link account</button>
            <button type="button" onClick={()=>{
                deleteStoredInfo()
                setUserInfo(null)
            }}>Logout</button>
        </main>
    )
}