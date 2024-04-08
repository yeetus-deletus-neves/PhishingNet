//import {useState} from 'react';
import { AuthProvider, useAuth } from "../components/AuthProvider";
import React from 'react';
import { PageSelector } from "./PageSelector.jsx";



const App = () => {
  return (
    <main>
      <AuthProvider>
        <PageSelector/>
      </AuthProvider>
    </main>
  );
};



export default App;