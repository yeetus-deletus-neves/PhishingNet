

const App = () => {

    return (
        <main>
            <h1>Phising Net</h1>
            <button onClick={()=>{
                console.log('Tried to login');
                //fetch('get token to API')
            }}>
                Login
            </button>
            <br></br>
            <button onClick={()=>{
                console.log('Tried to sign up');
                //window.open('sign up SPA url','_blank')
            }}>
                Sign Up
            </button>
        </main>
    )
}


export default App;