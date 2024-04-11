var currentURL = location.href;

function mailOpen() {
    const url = location.href;
    
    const startIndex = url.indexOf("id/");
    // Find the index of "?" after "id/"
    const endIndex = url.indexOf("?", startIndex);
    // Extract the substring
    const conversationID = url.substring(startIndex+3, endIndex);
    console.log(conversationID);
};

var observer = new MutationObserver(()=>{
    var newURL = location.href;
    if ( currentURL != newURL){
        currentURL = newURL;
        mailOpen()
    }
});

observer.observe(document, {subtree: true, childList: true});