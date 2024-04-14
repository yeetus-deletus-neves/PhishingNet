import { defaultFetch } from "../utils/fetch";

const App = () => {

    return (
        <main>
            <h1>Phising Net</h1>
                Username: <input type="text" name="username" id="username"/>
                Password: <input type="text" name="password" id="password"/>
                <button type="button" onClick={ async ()=>{
                let username = document.getElementById('username').value;
                let password = document.getElementById('password').value;
                console.log(`Username: ${username} / Password: ${password}`);

                const tokenRsp = await defaultFetch(
                    'http://localhost:8080/user',
                    "POST",
                    {
                        'Content-Type': 'application/json'
                    },
                    {
                        username: username,
                        password: password
                    }
                )
            }}>Submit</button>
            <br></br>
            <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{

                console.log('Tried to sign up');
                //window.open('sign up SPA url','_blank')
            
            }}>Sign up here</a></div>
        </main>
    )
}

export default App;