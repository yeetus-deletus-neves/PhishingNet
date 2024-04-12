import {Input, InputAdornment, InputLabel} from "@mui/material";
import {AccountCircle} from "@mui/icons-material";
import LockIcon from '@mui/icons-material/LockPerson';

const App = () => {

    return (
        <main>
            <h1>Phising Net</h1>
            <form onSubmit={()=>{
                console.log('Tried to login');
                //fetch('get token to API')
            }}>
                <div>
                    <InputLabel htmlFor="username" className='mui-theme'>
                                        Username
                    </InputLabel>
                    <Input
                        className='mui-theme mui-input'
                        id="username"
                        required={true}
                        name="username"
                        startAdornment={
                            <InputAdornment position="start">
                                <AccountCircle className='mui-theme'/>
                            </InputAdornment>
                        }
                    />
                </div>
                <div>
                    <InputLabel htmlFor="password" className='mui-theme'>
                        Password
                    </InputLabel>
                    <Input
                        className='mui-theme mui-input'
                        id="password"
                        type="password"
                        required={true}
                        name="password"
                        startAdornment={
                            <InputAdornment position="start">
                                <LockIcon className='mui-theme'/>
                            </InputAdornment>
                        }
                    />
                </div>
            </form>
            <br></br>
            <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{

                console.log('Tried to sign up');
                //window.open('sign up SPA url','_blank')
            
            }}>Sign up here</a></div>
        </main>
    )
}


export default App;