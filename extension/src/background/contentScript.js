import { defaultFetch } from "../utils/fetch";

browser.runtime.onMessage.addListener(
    function(message, sender, sendResponse) {
        console.log(sender.tab ?
            "from a content script:" + sender.tab.url :
            "from the extension");
        console.log(message.type)
        switch(message.type) {
            case "getInboxMail": {
                let email = document.getElementById("mectrl_currentAccount_secondary");
                if(!email){
                    const triggerEvent = (el, eventType, detail) =>
                        el.dispatchEvent(new CustomEvent(eventType, { detail }));
                      
                    var button = document.getElementById("O365_MainLink_Me")  
                    triggerEvent(button,'click')
                    triggerEvent(button,'click')
                    email = document.getElementById("mectrl_currentAccount_secondary").innerHTML;
                }else{
                    email = email.innerHTML
                }
                console.log(email);
                sendResponse(email);
                break;
            }
            case "background": {
                const content =  document.getElementsByClassName("analyse_content")[0]
                if(content && !location.href.includes(content.id)){
                    content.remove()
                }

                let email = document.getElementById("mectrl_currentAccount_secondary");
                if(!email){
                    const triggerEvent = (el, eventType, detail) =>
                        el.dispatchEvent(new CustomEvent(eventType, { detail }));
                      
                    var button = document.getElementById("O365_MainLink_Me")  
                    triggerEvent(button,'click')
                    triggerEvent(button,'click')
                    email = document.getElementById("mectrl_currentAccount_secondary").innerHTML;
                }else{
                    email = email.innerHTML
                }
                if(email = message.email){
                    let button = document.getElementById("analyse") 
                    if(!button){
                        const first = document.createElement("button");
                        var text = document.createTextNode("Analyse message");
                        first.appendChild(text);
                        first.setAttribute("id","analyse")
                        first.addEventListener("click",function(){
                            browser.runtime.sendMessage({
                                type: "buttonClicked",
                                value: location.href,
                                token: message.token,
                                tabId: message.tabId
                            })
                        })
                        document.getElementsByClassName("NTPm6 WWy1F")[0].appendChild(first);
                    }
                }
                break;
            }
            case "analysed":{
                let button = document.getElementById("analyse")
                button.remove()
                const first = document.createElement("h4");
                var text = document.createTextNode(message.content);
                first.appendChild(text);
                first.setAttribute("id",message.conversationID);
                first.setAttribute("class","analyse_content");
                document.getElementsByClassName("NTPm6 WWy1F")[0].appendChild(first);
            }
        }
    }
);


