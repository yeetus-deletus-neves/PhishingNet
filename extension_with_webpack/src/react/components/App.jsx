//import {useState} from 'react';

const App = () => {
  //const [scriptResp, setScriptResp] = useState('Not yet authorized');
  
  const authenticate = async () => {


    // Open tab for authentication
    chrome.tabs.create({url:
      'https://login.microsoftonline.com/common/oauth2/v2.0/authorize?' + new URLSearchParams({
        client_id: 'cb14d1d3-9a43-4b04-9c52-555211443e63',
        response_type: 'code',
        redirect_uri: 'https://outlook.office.com/mail',
        response_mode: 'query',
        scope: 'offline_access user.read mail.read',
        state: '12345'
      })
    });

    //const resp = await fetch('https://login.microsoftonline.com/common/oauth2/v2.0/authorize?client_id=cb14d1d3-9a43-4b04-9c52-555211443e63&response_type=code&redirect_uri=https%3A%2F%2Foutlook.office.com%2Fmail%2F&response_mode=query&scope=offline_access%20user.read%20mail.read&state=12345');
    
    //const users = await resp.json();
    //console.log(users);
    // Get email
    //const randomEmail = users[Math.floor(Math.random() * users.length)].email;
    // Get active tab
    //const tabs = await chrome.tabs.query({active: true, currentWindow: true});
    //const activeTab = tabs[0];
    // Get the response
    //const tabResp = await chrome.tabs.sendMessage(activeTab.id,'');
    //setScriptResp(tabResp);
  };

  return (
    <main>
      <h1>Phishing Net</h1>
      <button onClick={authenticate}>Authorize</button>
    </main>
  );
};
export default App;