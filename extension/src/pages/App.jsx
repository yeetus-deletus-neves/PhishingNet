import { useState } from "react";
import { defaultFetch } from "../utils/fetch";


  
const App = () => {
    const [url,setUrl] = useState(null)
    const [hasCode,setCode] = useState(null)

    function logTabs(tabs) {
        let tab = tabs[0]; // Safe to assume there will only be one result
        console.log(tab.url)
        setUrl(tab.url);
    }
    browser.tabs.query({currentWindow: true, active: true}).then(logTabs, console.error);
    const storedInfo  = window.localStorage.getItem("userToken")
    if(!storedInfo){
        return (
            <main>
                <h1>Phising Net</h1>
                    Username: <input type="text" name="username" id="username"/>
                    Password: <input type="text" name="password" id="password"/>
                    <button type="button" onClick={ async ()=>{
                    let username = document.getElementById('username').value;
                    let password = document.getElementById('password').value;
    
                    const tokenRsp = await defaultFetch(
                        'http://localhost:8080/user/signIn',
                        "POST",
                        {
                            'Content-Type': 'application/json'
                        },
                        {
                            "username": username,
                            "password": password
                        }
                    )
                    window.localStorage.setItem("userToken",tokenRsp.token)
                    setCode(true)
                }}>Submit</button>
                <br></br>
                <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                    window.open('http://localhost:3000/signUp','_blank')
                }}>Sign up here</a></div>
            </main>
        )
    }else{
        if(!storedInfo.email){
            return (
                <main>
                    <h1>Phishing Net</h1>
                    <h3>Hi {storedInfo.username} your account is not linked</h3>
                    <button type="button" onClick={()=>{
                        window.open('http://localhost:3000/','_blank')
                    }}>Link account</button>
                </main>
            )
        }else{
            if(url && url.includes("outlook.live.com/mail/") && url.includes("id/" )){
                return (
                <main>
                    <h1>Phishing Net</h1>
                    <button type="button" onClick={ async ()=>{
                        const startIndex = url.indexOf("id/");
    
                        // Extract the substring
                        const conversationID = url.substring(startIndex+3);
                
                        const analyseRsp = await defaultFetch(
                            'http://localhost:8080/analyse',
                            "POST",
                            {
                                'Content-Type': 'application/json',
                                'Authorization': `Bearer ${storedInfo.token.token}`,
                            },
                            {
                                "content":`${conversationID}`
                            }
                        )
                        console.log(analyseRsp)
                    }}>Analyse Content</button>
                    <button type="button" onClick={()=>{
                        window.localStorage.removeItem("userToken")
                        setCode(false)
                    }}>Logout</button>
                </main>)
            }else{
                return(
                <main>
                    <h1>Phishing Net</h1>
                    <h3>Select a message</h3>
                    <button type="button" onClick={()=>{
                        window.localStorage.removeItem("userToken")
                        setCode(false)
                    }}>Logout</button>
                </main>
                )
            }
        }
    }
}

export default App;