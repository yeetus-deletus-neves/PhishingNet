import {BrowserRouter, createBrowserRouter, Link, Route, Routes, RouterProvider} from 'react-router-dom'
import {Layout} from './pages/Layout'
import React from 'react';
import { ErrorPage } from './ErrorHandling/ErrorPage';
import { AuthProvider } from './auth/AuthProvider';
import { RequiresAuth } from './auth/RequiresAuth';
import { UnlinkPage } from './pages/Unlink';
import { HomePage } from './pages/Home';
import { LoginPage } from './pages/Login';
import { SignUpPage } from './pages/SignUp';
import { LinkPage } from './pages/Link';
import { AboutPage } from './pages/About';

const router = createBrowserRouter([
    {
        element: <Layout/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                path:'/',
                element:<HomePage/>
            },
            {
                path:'/login',
                element:<LoginPage/>
            },
            {
                path:'/signup',
                element:<SignUpPage/>
            },
            {
                path:'/about',
                element:<AboutPage/>
            },
            {
                path:'/link',
                element:
                <RequiresAuth>
                    <LinkPage/>
                </RequiresAuth>
            },            {
                path:'/unlink',
                element:
                <RequiresAuth>
                    <UnlinkPage/>
                </RequiresAuth>
            },
        ]
    }
])


export default function App(){
    return (
        <AuthProvider>
            <RouterProvider router={router}/>
        </AuthProvider>
    );
}

