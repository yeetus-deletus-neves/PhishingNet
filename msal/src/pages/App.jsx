//import {useState} from 'react';
import { AuthProvider, useAuth } from "../components/AuthProvider";
import React from 'react';
import { PageSelector } from "./PageSelector.jsx";
import { MsalInterface } from "../scripts/msal.js";



const App = () => {
  const msal_agent = new MsalInterface();
  return (
    <main>
      <AuthProvider>
        <PageSelector msal_agent={msal_agent}/>
      </AuthProvider>
    </main>
  );
};



export default App;