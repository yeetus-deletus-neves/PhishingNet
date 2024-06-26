import { Link, Outlet } from "react-router-dom";
import { useAuthentication } from "../auth/AuthProvider";
import { createContext, useContext, useState } from "react";
import { ClientLog } from "../ErrorHandling/ClientLog";

const AlertContext = createContext([undefined,()=>{}])

export function Layout(){
    const [userInfo,setUserInfo] = useAuthentication()
    const [alert,setAlert] = useState(undefined)
   
    return (
        <AlertContext.Provider value={[alert,setAlert]}>
            <div>
                <nav className="navbar navbar-expand-lg navbar-light bg-light">
                    <a className="navbar-brand">Phishing Net</a>
                    <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon"></span>
                    </button>
                    <div className="collapse navbar-collapse" id="navbarNavAltMarkup">
                        <div className="navbar-nav">
                            <Link className="nav-item nav-link" to="/">Home</Link>
                            <Link className="nav-item nav-link" to="/login">{userInfo ? "Logout" : "Login"}</Link>
                            {userInfo ? <></> : <Link className="nav-item nav-link" to="/signUp">Signup</Link>}
                            {userInfo?.email ? <Link className="nav-item nav-link" to="/unlink">Unlink</Link> : <></>}
                            <Link className="nav-item nav-link" to="/about">About</Link>
                        </div>
                    </div>
                </nav>
                {alert ? <ClientLog context={{alert: alert.alert, message: alert.message}} onClose={()=> {setAlert(undefined)}}/> : null}
                <Outlet/>
            </div>
        </AlertContext.Provider>
    );        
}

export function useAlertContext(){
    return useContext(AlertContext)
}
