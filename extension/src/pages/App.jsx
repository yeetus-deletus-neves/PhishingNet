import { useState} from "react";
import { useAuthentication } from "../auth/authenticationProvider";
import { NotLinked } from "../components/NotLinked";
import { Analyse } from "../components/Analyse";
import { SelectMessage } from "../components/SelectMessage";
import { WrongInbox } from "../components/WrongInbox";
import { Login } from "../components/Login";
import { useAlertContext } from "../components/Layout";

  
const App = () => {
    const [url,setUrl] = useState(null)
    const [userInfo,setUserInfo] = useAuthentication()
    const [inboxMail,setInboxMail] = useState(null)
    const [alert, setAlert] = useAlertContext()

    function logTabs(tabs) {
        let tab = tabs[0]; // Safe to assume there will only be one result
        setUrl(tab.url);
        if(url && url.includes("outlook.live.com/mail/")){
            browser.tabs.sendMessage(tab.id, {type:"getInboxMail"},function(rsp){
                setInboxMail(rsp)
            })
            /*
            if(url.includes("id/") && userInfo.email){
                console.log("second message")
                browser.tabs.sendMessage(tab.id, {type:"putAnalyseButton"},async function(rsp){
                    try{
                        const analyseRsp = await defaultFetch(
                            'http://localhost:8080/analyse',
                            "POST",
                            {
                                'Content-Type': 'application/json',
                                'Authorization': `Bearer ${userInfo.token.token}`,
                            },
                            {
                                "content":`${rsp}`
                            }
                        )

                    }catch(error){
                        setAlert({alert: "error", message: `${error.details}`})
                    }
                })
            }*/
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