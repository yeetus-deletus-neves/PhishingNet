import React from "react"
import { useState, useContext,createContext } from "react"
import { ClientLog } from "../errorHandling/ClientLog"
import { deleteStoredInfo, deleteStoredMap } from "../utils/localstorage"
import { useAuthentication } from "../auth/authenticationProvider"

const AlertContext = createContext([undefined, () => {}])

export function Layout({children}){
    const [alert, setAlert] = useState(undefined)
    const [userInfo,setUserInfo] = useAuthentication()

    return(
        <AlertContext.Provider value={[alert, setAlert]}>
            <div class="card text-center" style={{width: "15rem"}}>
                <img class="card-img-top" src="./icons/phishing-net-icon.png" alt="Card image cap" onClick={()=>{
                    window.open('http://localhost:3000/','_blank')
                }}/>
                <div class="card-body" >
                    <h5 class="card-title">Phishing Net</h5>
                    {children}
                    {alert ? <ClientLog context={{alert: alert.alert, message: alert.message}} onClose={()=> {setAlert(undefined)}}/> : null}
                    <br></br>
                    {userInfo ? 
                        <button type="submit" class="btn btn-dark" style={{width: "12rem"}} onClick={()=>{
                            deleteStoredInfo()
                            setUserInfo(null)
                            deleteStoredMap()
                            setAlert({alert: "warning", message: "utilizador desconectado"})
                        }}>Logout</button>
                        :
                        <div>
                            Não tem conta? <a style={{color: "#3d3df9", cursor: "pointer"}} onClick={()=>{
                                window.open('http://localhost:3000/signUp','_blank')
                            }}>Crie uma aqui</a>
                        </div>
                    }
                </div>
            </div>

        </AlertContext.Provider>
    )
}

export function useAlertContext(){
    return useContext(AlertContext)
}
