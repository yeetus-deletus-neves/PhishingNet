import { useState } from "react";
import { defaultFetch } from "../utils/fetch";


  
const App = () => {
    const [url,setUrl] = useState(null)

    function logTabs(tabs) {
        let tab = tabs[0]; // Safe to assume there will only be one result
        console.log(tab.url)
        setUrl(tab.url);
    }
    browser.tabs.query({currentWindow: true, active: true}).then(logTabs, console.error);
    const token  = window.localStorage.getItem("userToken")
    if(token){
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
                            'Authorization': `Bearer ${token}`,
                        },
                        {
                            "content":`${conversationID}`
                        }
                    )
                    console.log(analyseRsp)
                }}>Analyse Content</button>
            </main>)
        }else{
            return(
            <main>
                <h1>Phishing Net</h1>
                <h3>Select a message</h3>
            </main>
            )
        }
    }

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
                console.log(tokenRsp.token)
                window.localStorage.setItem("userToken",tokenRsp.token)
            }}>Submit</button>
            <br></br>
            <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                window.open('http://localhost:3000/signUp','_blank')
            }}>Sign up here</a></div>
        </main>
    )
}

export default App;