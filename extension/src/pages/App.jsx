import { useState} from "react";
import { useAuthentication } from "../auth/authenticationProvider";
import { NotLinked } from "../components/NotLinked";
import { Analyse } from "../components/Analyse";
import { SelectMessage } from "../components/SelectMessage";
import { WrongInbox } from "../components/WrongInbox";
import { Login } from "../components/Login";

  
const App = () => {
    const [url,setUrl] = useState(null)
    const [userInfo,setUserInfo] = useAuthentication()
    const [inboxMail,setInboxMail] = useState(null)

    function logTabs(tabs) {
        let tab = tabs[0]; // Safe to assume there will only be one result
        console.log(tab.url)
        setUrl(tab.url);
        console.log(tab)
        if(url && url.includes("outlook.live.com/mail/")){
            browser.tabs.sendMessage(tab.id, {type:"getInboxMail"},function(rsp){
                setInboxMail(rsp)
            })
        }else{
            setInboxMail(null)
        }
    }
    browser.tabs.query({currentWindow: true, active: true}).then(logTabs, console.error);
    
    if(!userInfo){
        return (
            <Login/>
        )
    }else{
        if(!userInfo.email){
            return (
                <NotLinked/>
            )
        }else{
            if(inboxMail == userInfo.email){
                if(url.includes("id/" )){
                    return (
                        <Analyse url={url}/>
                    )
                }else{
                    return(
                        <SelectMessage/>
                    )
                }
            }else{
                return (
                    <WrongInbox inboxMail={inboxMail}/>
                )
            }
        }
    }
}

export default App;