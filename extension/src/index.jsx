import {createRoot} from 'react-dom/client';
import App from './pages/App';
import React from 'react';
import { AuthProvider } from './utils/auth';

const root = createRoot(document.getElementById('root'));
root.render(
    <AuthProvider>
        <App/>
    </AuthProvider>
);
