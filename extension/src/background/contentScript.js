function createWarningBar(message){
    const div = document.createElement("div");
    const header = document.createElement("h2"); 
    var text = document.createTextNode(message);
    header.appendChild(text);
    div.setAttribute("id","phishing-net-warning")
    div.setAttribute("style","height: 48px; line-height: 48px; display: flex; justify-content: center; align-items: center;")
    div.appendChild(header);
    return div
}

browser.runtime.onMessage.addListener(
    function(message, sender, sendResponse) {
        console.log(sender.tab ?
            "from a content script:" + sender.tab.url :
            "from the extension");
        console.log(message.type)
        switch(message.type) {
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
                    email = document.getElementById("mectrl_currentAccount_secondary").innerHTML;
                    triggerEvent(button,'click')
                }else{
                    email = email.innerHTML
                }
                let warningBar = document.getElementById("phishing-net-warning")
                if(warningBar){
                    warningBar.remove()
                }
                if(email = message.email){
                    warningBar = createWarningBar("Logged in phishing net")

                    if(location.href.includes("id/")){

                        let button = document.getElementById("analyse") 
                        if(!button){
                            const first = document.createElement("button");
                            var text = document.createTextNode("Analyse message");
                            first.appendChild(text);
                            first.setAttribute("id","analyse")
                            first.setAttribute("style","color: black; background: white")
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
                }else{
                    // incorrect email o365header
                    warningBar = createWarningBar(`Incorrect email, should be on the inbox of ${message.email}, instead you are on the inbox of ${email}`)
                }
                let doc = document.getElementById("appContainer")
                doc.insertBefore(warningBar,doc.firstChild)
                break;
            }
            case "analysed":{
                let button = document.getElementById("analyse")
                button.remove()
                const div = document.createElement("div");
                const header = document.createElement("h4");
                var text = document.createTextNode(message.content.threat);
                header.setAttribute("style", "color:red")
                header.appendChild(text);
                div.setAttribute("id",message.conversationID);
                div.setAttribute("class","analyse_content");
                const threats = message.content.threatJustification
                if(threats){
                    let title = ""
                    threats.forEach(t => {
                        title += `${t.name}: ${t.description}\n`
                    });
                    div.setAttribute("title",title)
                }
                div.appendChild(header)
                document.getElementsByClassName("NTPm6 WWy1F")[0].appendChild(div);
                break;
            }
            case "clean":{
                let button = document.getElementById("analyse")
                if(button){
                    button.remove()
                }
                let content = document.getElementsByClassName("analyse_content")[0]
                if(content){
                    content.remove()
                }
                let warningBar = document.getElementById("phishing-net-warning")
                if(warningBar){
                    warningBar.remove()
                }

                if(message.user){
                    // not linked
                    warningBar = createWarningBar("No account linked to your phishing net account")
                    const first = document.createElement("button");
                    var text = document.createTextNode("Link account");
                    first.appendChild(text);
                    first.setAttribute("id","link_account")
                    first.setAttribute("style","color: black; background: white")
                    first.addEventListener("click",function(){
                        window.open('http://localhost:3000/','_blank')
                    })
                    warningBar.appendChild(first)

               }
                else{
                    // not logged on
                    warningBar = createWarningBar("Not logged in any phishing net account")
                }
                let doc = document.getElementById("appContainer")
                doc.insertBefore(warningBar,doc.firstChild)
                break;
            }
        }
    }
);


