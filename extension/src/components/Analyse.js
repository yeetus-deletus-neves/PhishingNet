import { useAuthentication } from "../utils/auth";
import { About } from "./About";
import { defaultFetch } from "../utils/fetch";
import { deleteStoredInfo } from "../utils/localstorage";

export function Analyse({url}){
    const [userInfo,setUserInfo] = useAuthentication()
    return (
        <main>
            <About/>
            <button type="button" onClick={ async ()=>{
                const startIndex = url.indexOf("id/");

                // Extract the substring
                const conversationID = url.substring(startIndex+3);
        
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
            }}>Analyse Content</button>
            <button type="button" onClick={()=>{
                deleteStoredInfo()
                setUserInfo(null)
            }}>Logout</button>
        </main>
    )
}