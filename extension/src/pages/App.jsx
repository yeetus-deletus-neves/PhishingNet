import { defaultFetch } from "../utils/fetch";

const App = () => {
    if(localStorage.getItem("userToken")){
        return (<div>Authorized</div>)
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
                localStorage.setItem("userToken",tokenRsp)
            }}>Submit</button>
            <br></br>
            <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                window.open('http://localhost:3000/signUp','_blank')
            }}>Sign up here</a></div>
        </main>
    )
}

export default App;