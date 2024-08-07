import { PublicClientApplication } from "@azure/msal-node";

//TODO: tokens should not be here
const msalConfig = {
    auth: {
        clientId: 'cb14d1d3-9a43-4b04-9c52-555211443e63', // Substitua pelo seu Client ID
        authority: 'https://login.microsoftonline.com/common', // Substitua pelo seu Azure AD tenant
        redirectUri: 'http://localhost:3000/link', // URL de redirecionamento após o login
    },
    cache: {
        cacheLocation: 'sessionStorage',
        storeAuthStateInCookie: false
    }
};

const loginRequest = {
    scopes: ['user.read', 'mail.read','offline_access'] // Escopos de permissão necessários (por exemplo, para o Microsoft Graph)
};

const authorizationReq = {
    scopes : loginRequest.scopes,
    redirectUri : msalConfig.auth.redirectUri
};

export class MsalInterface {

    constructor(){
        this.myMSALObj = new PublicClientApplication(msalConfig)
    }
    

    login() {
        this.myMSALObj.getAuthCodeUrl(authorizationReq)
            .then(response => {
                console.log('Response', response);
                window.location.replace(response);
                // Aqui você pode usar o access token para fazer chamadas à API protegida, como o Microsoft Graph
            })
            .catch(error => {
                console.log('Error during login:', error);
            });
    };
}