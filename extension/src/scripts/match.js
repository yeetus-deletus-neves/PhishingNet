import { defaultFetch } from "../utils/fetch";

var currentURL = location.href;

async function mailOpen() {
    const url = location.href;
    if(url.includes("id/")){
        // Find the index of "id/"
        const startIndex = url.indexOf("id/");

        // Extract the substring
        const conversationID = url.substring(startIndex+3);
        console.log(conversationID);

        const analyseRsp = await defaultFetch(
            `http://localhost:8080/analyse?conversationId=${conversationID}`,
            "GET",
            {
                'Content-Type': 'application/json',
                'Authorization': `${localStorage.getItem('userToken')}`,
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