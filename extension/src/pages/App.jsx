import { useState } from "react";
import { defaultFetch } from "../utils/fetch";
import { deleteStoredInfo, getStoredInfo, setStoredInfo } from "../utils/localstorage";
import { About } from "../components/About";


  
const App = () => {
    const [url,setUrl] = useState(null)
    const [userInfo,setUserInfo] = useState(getStoredInfo())

    function logTabs(tabs) {
        let tab = tabs[0]; // Safe to assume there will only be one result
        console.log(tab.url)
        setUrl(tab.url);
    }
    browser.tabs.query({currentWindow: true, active: true}).then(logTabs, console.error);
    if(!userInfo){
        return (
            <main>
                <About/>
                    <input placeholder="Username" type="text" name="username" id="username"/>
                    <input placeholder="Password" type="password" name="password" id="password"/>
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
                    tokenRsp.username = username
                    setStoredInfo(tokenRsp)
                    setUserInfo(tokenRsp)
                }}>Submit</button>
                <br></br>
                <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                    window.open('http://localhost:3000/signUp','_blank')
                }}>Sign up here</a></div>
            </main>
        )
    }else{
        if(!userInfo.email){
            return (
                <main>
                    <About/>
                    <h3>Hi {userInfo.username} your account is not linked</h3>
                    <button type="button" onClick={()=>{
                        window.open('http://localhost:3000/','_blank')
                    }}>Link account</button>
                </main>
            )
        }else{
            if(url && url.includes("outlook.live.com/mail/") && url.includes("id/" )){
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
                </main>)
            }else{
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
        }
    }
}

export default App;