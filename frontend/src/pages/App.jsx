import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import {Layout} from '../components/Layout'
import { ErrorPage } from '../components/ErrorHandling/ErrorPage';
import { HomePage } from '../components/Home';
import { SignUpPage } from '../components/SignUp';
import { LoginPage } from '../components/Login';



const router = createBrowserRouter([
    {
        element: <Layout/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path: "/",
                element: <HomePage/>
            },
            {
                path: "/signUp",
                element: <SignUpPage/>
            },
            {
                paht: "/login",
                element: <LoginPage/>
            }
        ]
    }
])


export function App(){
    return (
        <RouterProvider router={router}/>
    );
}

