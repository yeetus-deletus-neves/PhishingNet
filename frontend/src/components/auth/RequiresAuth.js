import { useAuthentication } from "./AuthProvider";
import {useLocation, Navigate} from "react-router-dom"

export function RequiresAuth({children}){
    const [userInfo] = useAuthentication()
    const location = useLocation()

    if(userInfo){
        return <>{children}</>
    }else{
        return <Navigate to="/" replace={true} state={{from: location}}/>
    }
}