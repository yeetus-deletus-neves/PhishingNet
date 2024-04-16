import { createRoot } from "react-dom/client";
import App from "./pages/App";
import React from "react";
import { MsalInterface } from "./scripts/msal";

const msalAgent = new MsalInterface();
const root = createRoot(document.getElementById('root'));
root.render(<><App msalAgent={msalAgent}/></>);


