
browser.runtime.onMessage.addListener(
    function(message, sender, sendResponse) {
        switch(message.type) {
            case "getInboxMail":
                sendResponse(document.getElementById("mectrl_currentAccount_secondary").innerHTML);
            break;
        }
    }
);