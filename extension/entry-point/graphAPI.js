mailOpen(); 



var currentURL = location.href;

var observer = new MutationObserver(()=>{
    newURL = location.href;
    if ( currentURL != newURL){
        currentURL = newURL;
        mailOpen()
    }
})

observer.observe(document, {subtree: true, childList: true});

async function mailOpen() {
    const url = location.href;
    
    const startIndex = url.indexOf("id/");
    // Find the index of "?" after "id/"
    const endIndex = url.indexOf("?", startIndex);
    // Extract the substring
    const conversationID = url.substring(startIndex+3, endIndex);
    console.log(conversationID);
    const startIndexCode = url.indexOf("code=");
    const endIndexCode = url.length;
    // Extract the substring
    const code = url.substring(startIndexCode+5, endIndexCode);
    console.log(code);

    var details = {
        "client_id" : "cb14d1d3-9a43-4b04-9c52-555211443e63",
        "scope" : "user.read mail.read",
        "code" : `${code}`,
        "redirect_uri":"https://outlook.office.com/mail",
        "grant_type":"authorization_code",
        "client_secret":"P~N8Q~_.z7Xp6gpIUo~u8N7u6~bS1VpsKEy~1ak1"
    };
    var formBody = [];
    for (var property in details) {
        var encodedKey = encodeURIComponent(property);
        var encodedValue = encodeURIComponent(details[property]);
        formBody.push(encodedKey + "=" + encodedValue);
    }
    formBody = formBody.join("&");

    fetch("https://login.microsoftonline.com/common/oauth2/v2.0/token/",{
        method: "POST",
        mode:"cors",
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Headers': '*' 
        },
        body: formBody
    })
    .then(data =>{return data.json()})
    .then(res => {console.log(res)});
}