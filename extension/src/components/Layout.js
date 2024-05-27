import { useState } from "react"
import { createContext } from "react"
import { useContext } from "react"
import { ClientLog } from "../errorHandling/ClientLog"
import { deleteStoredInfo } from "../utils/localstorage"
import { useAuthentication } from "../auth/authenticationProvider"

const AlertContext = createContext([undefined, () => {}])

export function Layout({children}){
    const [alert, setAlert] = useState(undefined)
    const [userInfo,setUserInfo] = useAuthentication()

    return(
        <AlertContext.Provider value={[alert, setAlert]}>
            <div>
                <button type="button" class="btn btn-dark" onClick={()=>{
                    window.open('http://localhost:3000/','_blank')
                }}>
                    Phishing Net <span class="badge badge-light">?</span>
                </button> 
            </div>
            {children}
            {alert ? <ClientLog context={{alert: alert.alert, message: alert.message}} onClose={()=> {setAlert(undefined)}}/> : null}
            <br></br>
            {userInfo ? 
                <button type="button" onClick={()=>{
                    deleteStoredInfo()
                    setUserInfo(null)
                    setAlert({alert: "warning", message: "user logged out"})
                }}>Logout</button>
                :
                <div>Not a member? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                        window.open('http://localhost:3000/signUp','_blank')
                    }}>Sign up here</a>
                </div>
            }
        </AlertContext.Provider>
    )
}

export function useAlertContext(){
    return useContext(AlertContext)
}
