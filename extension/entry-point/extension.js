import {PublicClientApplication} from "@azure/msal-browser";

console.log("Hello");

const msalConfig = {
    auth: {
        clientId: 'cb14d1d3-9a43-4b04-9c52-555211443e63'
    }
};

const msalInstance = new PublicClientApplication(msalConfig);

async function authenticate(){
    await msalInstance.initialize();
    console.log("Here my boy");
    try {
        const loginResponse = await msalInstance.loginPopup({
            redirectUri: "https://outlook.office.com/mail",
            scopes: ["user.read", "mail.read"]
        });
    } catch (err) {
        // handle error
    }
    
};



authenticate();
