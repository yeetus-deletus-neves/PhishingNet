import { useState} from "react";
import { HomePage } from "../components/HomePage";
import { useAuthentication } from "../utils/auth";
import { NotLinked } from "../components/NotLinked";
import { Analyse } from "../components/Analyse";
import { SelectMessage } from "../components/SelectMessage";

  
const App = () => {
    const [url,setUrl] = useState(null)
    const [userInfo,setUserInfo] = useAuthentication()

    function logTabs(tabs) {
        let tab = tabs[0]; // Safe to assume there will only be one result
        console.log(tab.url)
        setUrl(tab.url);
    }
    browser.tabs.query({currentWindow: true, active: true}).then(logTabs, console.error);
    
    if(!userInfo){
        return (
            <HomePage/>
        )
    }else{
        if(!userInfo.email){
            return (
                <NotLinked/>
            )
        }else{
            if(url && url.includes("outlook.live.com/mail/") && url.includes("id/" )){
                return (
                    <Analyse url={url}/>
                )
            }else{
                return(
                    <SelectMessage/>
                )
            }
        }
    }
}

export default App;