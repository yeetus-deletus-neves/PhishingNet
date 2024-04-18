import { Link, Outlet } from "react-router-dom";

export function Layout(){
    return (
        <div>
            <div className="navbar">
                <div className="topnav">
                    <Link to="/">Home</Link>
                    <Link to="/login">Login</Link>
                    <Link to="/signUp">SignUp</Link>
                </div>
            </div>
            <Outlet/>
        </div>
    );
}