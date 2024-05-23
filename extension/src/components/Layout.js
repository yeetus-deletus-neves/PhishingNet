import { useState } from "react"
import { createContext } from "react"
import { useContext } from "react"
import { ClientLog } from "../errorHandling/ClientLog"

const AlertContext = createContext([undefined, () => {}])

export function Layout({children}){
    const [alert, setAlert] = useState(undefined)

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
        </AlertContext.Provider>
    )
}

export function useAlertContext(){
    return useContext(AlertContext)
}
