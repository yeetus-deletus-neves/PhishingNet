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

function mailOpen() {
    console.log("Hey, I am here!");
    console.log(location.href);
}