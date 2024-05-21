import { useAlertContext } from "../Layout";
import { useAuthentication } from "./AuthProvider";
import {useLocation, Navigate} from "react-router-dom"

export function RequiresAuth({children}){
    const [userInfo] = useAuthentication()
    const [alert, setAlert] = useAlertContext()
    const location = useLocation()

    if(userInfo){
        return <>{children}</>
    }else{
        setAlert({alert: "warning", message: "Authentication required!"})
        return <Navigate to="/login" replace={true} state={{from: location}}/>
    }
}