import { useAuthentication } from "../utils/auth"
import { About } from "./About"

export function WrongInbox({inboxMail}){
    const [userInfo, setUserInfo] = useAuthentication()

    return (
        <main>
            <About/>
            <h3>You should be in the inbox of the email {userInfo.email}</h3>
            {inboxMail ? <h3>Instead you are in the inbox of {inboxMail}</h3> : <h3>Currently not in an inbox</h3>}
        </main>    
    )
}