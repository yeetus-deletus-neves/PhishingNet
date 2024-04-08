import { PublicClientApplication } from "@azure/msal-node";

//TODO: tokens should not be here
const msalConfig = {
    auth: {
        clientId: 'cb14d1d3-9a43-4b04-9c52-555211443e63', // Substitua pelo seu Client ID
        authority: 'https://login.microsoftonline.com/common', // Substitua pelo seu Azure AD tenant
        redirectUri: 'https://outlook.office.com/mail', // URL de redirecionamento após o login
    },
    cache: {
        cacheLocation: 'sessionStorage',
        storeAuthStateInCookie: false
    }
};

const loginRequest = {
    scopes: ['user.read'] // Escopos de permissão necessários (por exemplo, para o Microsoft Graph)
};

export class MsalInterface {

    constructor(){
        this.myMSALObj = new PublicClientApplication(msalConfig)
    }
    

    login() {
        this.myMSALObj.acquireTokenPopup(loginRequest)
            .then(response => {
                console.log('Login successful!');
                console.log('Access token:', response.accessToken);
                // Aqui você pode usar o access token para fazer chamadas à API protegida, como o Microsoft Graph
            })
            .catch(error => {
                console.log('Error during login:', error);
            });
    }
}