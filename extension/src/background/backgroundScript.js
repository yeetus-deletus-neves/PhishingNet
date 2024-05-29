import { getStoredInfo } from "../utils/localstorage";
import { defaultFetch } from "../utils/fetch";

browser.tabs.onUpdated.addListener(
  function(tabId, changeInfo, tab) {
    // read changeInfo data and do something with it
    // like send the new url to contentscripts.js

    if(changeInfo.url && changeInfo.url.includes("outlook.live.com/mail/") && changeInfo.url.includes("id/")) {
      const userInfo = getStoredInfo()

      if(!userInfo || !userInfo.email){
        browser.tabs.sendMessage(tabId,{
          type: "clean",
          user: userInfo 
        })
      }else{
        browser.tabs.sendMessage(tabId, {
          type: "background",
          email: userInfo.email,
          token: userInfo.token.token,
          tabId: tabId
        })
      }
    }
  }
);

browser.runtime.onMessage.addListener(
  async function(message, sender, sendResponse){
    switch(message.type){
      case "buttonClicked":{
        console.log(message.tabId)
        const url = message.value
        const startIndex = url.indexOf("id/");
        // Extract the substring
        const conversationID = url.substring(startIndex+3);
        const analyseRsp = await defaultFetch(
          'http://localhost:8080/analyse',
          "POST",
          {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${message.token}`,
          },
          {
            "messageID":`${conversationID}`
          }
        )
        console.log(analyseRsp)
        browser.tabs.sendMessage(message.tabId,{
          type: "analysed",
          content: analyseRsp,
          conversationID: conversationID
        })
        break;
      }
    }
  }
)