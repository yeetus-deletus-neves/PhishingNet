import { defaultFetch } from "../utils/fetch";

var currentURL = location.href;

window.addEventListener("message",function(event){
    if(event.source != window){
        return;
    }
    if (event.data.type && (event.data.type == "FROM_PAGE")) {
        console.log("Content script received message: " + event.data.text);
    }
});

async function mailOpen() {
    //const token = localStorage.getItem('userToken')
    const url = location.href;
    if(url.includes("id/")){
        // Find the index of "id/"
        const startIndex = url.indexOf("id/");

        // Extract the substring
        const conversationID = url.substring(startIndex+3);

        const analyseRsp = await defaultFetch(
            `http://localhost:8080/analyse?conversationId=${conversationID}`,
            "GET",
            {
                'Content-Type': 'application/json',
                'Authorization': `123`,
            }
        )
        console.log(analyseRsp)

    }else{
        console.log('No conversation selected');
    }
};

var observer = new MutationObserver(async ()=>{
    var newURL = location.href;
    if ( currentURL != newURL){
        currentURL = newURL;
        await mailOpen()
    }
});

observer.observe(document, {subtree: true, childList: true});