const warningMap = new Map([
    ["NO_THREAT",'a'],
    ["SHOULD_LOOK_INTO_IT",'b'],
    ["SUSPICIOUS",'c'],
    ["VERY_SUSPICIOUS","d"],
    ["ALARMING","e"]
]
)



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

function createPhishingBar(){
    const div = document.createElement("div");
    div.setAttribute("id","phishing-net-bar")
    div.setAttribute("style","height: 48px; line-height: 48px; display: flex; justify-content: center; align-items: center;")
    return div
}

function changePhishingBar(element){
    const div = document.getElementById("phishing-net-bar")
    let nodes = div.childNodes
    nodes.forEach((n)=>{
        div.removeChild(n)
    })
    div.appendChild(element)
}


browser.runtime.onMessage.addListener(
    function(message, sender, sendResponse) {
        console.log(sender.tab ?
            "from a content script:" + sender.tab.url :
            "from the extension");
        console.log(message.type)
        switch(message.type) {
            case "background": {
                const content =  document.getElementsByClassName("analyze_content")[0]
                if(content && !location.href.includes(content.id)){
                    content.parentElement.remove()
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
                console.log(email)
                console.log(message.email)
                let warningBar = document.getElementById("phishing-net-warning")
                if(warningBar){
                    warningBar.remove()
                }
                if(email == message.email){
                    warningBar = createWarningBar("Logged in phishing net")

                    if(location.href.includes("id/")){

                        let button = document.getElementById("analyze") 
                        if(!button){
                            const first = document.createElement("img");
                            first.setAttribute("id","analyze")
                            let url = browser.runtime.getURL("icons/phishing_btn.png")
                            first.setAttribute("src",url)
                            url = browser.runtime.getURL("icons/warnings.gif")
                            first.addEventListener("click",function(){
                                document.getElementById("analyze").remove()
                                let gif = document.createElement("img")
                                gif.setAttribute("id","analyze")
                                gif.setAttribute("src",url)
                                gif.setAttribute("style","height: 48px;")
                                //document.getElementsByClassName("NTPm6 WWy1F")[0].appendChild(gif);
                                changePhishingBar(gif)
                                browser.runtime.sendMessage({
                                    type: "buttonClicked",
                                    value: location.href,
                                    token: message.token,
                                    tabId: message.tabId
                                })
                            })
                            const div = createPhishingBar()
                            div.appendChild(first)
                            document.getElementsByClassName("NTPm6 WWy1F")[0].after(div);
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
            case "analyzed":{
                const img  = document.createElement("img");
                img.setAttribute("id",message.conversationID);
                img.setAttribute("class","analyze_content");
                const url = browser.runtime.getURL(`icons/${warningMap.get(message.content.threat)}_warning.png`)
                img.setAttribute("src",url)
                img.setAttribute("style","height: 48px;")
                changePhishingBar(img)
/* 
const div = document.createElement("div");
const header = document.createElement("h4");
var text = document.createTextNode(message.content.threat);
header.setAttribute("style", "color:red")
header.appendChild(text);
div.setAttribute("id",message.conversationID);
div.setAttribute("class","analyze_content");
const threats = message.content.threatJustification
if(threats){
    let title = ""
    threats.forEach(t => {
        title += `${t.name}: ${t.description}\n`
        });
        div.setAttribute("title",title)
        }
        div.appendChild(header)
        */
                break;
            }
            case "clean":{
                let phishingBar = document.getElementById("phishing-net-bar")
                if(phishingBar){
                    phishingBar.remove()
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
                    first.setAttribute("style","color: white; background: blue")
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


