import { useAlertContext } from "../pages/Layout";
import { useAuthentication } from "./AuthProvider";
import {useLocation, Navigate} from "react-router-dom"

export function RequiresAuth({children}){
    const [userInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()
    const location = useLocation()

    if(userInfo){
        return <>{children}</>
    }else{
        setAlert({alert: "warning", message: "Autenticação necessária!"})
        return <Navigate to="/login" replace={true} state={{from: location}}/>
    }
}