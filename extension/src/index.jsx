import {createRoot} from 'react-dom/client';
import App from './pages/App';
import React from 'react';
import { AuthProvider } from './auth/authenticationProvider';
import { Layout } from './components/Layout';

const root = createRoot(document.getElementById('root'));
root.render(
    <AuthProvider>
        <Layout>
            <App/>
        </Layout>
    </AuthProvider>
);
