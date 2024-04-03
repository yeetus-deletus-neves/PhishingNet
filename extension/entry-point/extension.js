
function initializeGraph(){
  const tenant = "common";
  const client_id = "cb14d1d3-9a43-4b04-9c52-555211443e63";
  const redirect_uri = encodeURIComponent("https://outlook.office.com/mail/");
  const scope = encodeURIComponent("offline_access user.read mail.read");
  const state = "12345";

  const url = `https://login.microsoftonline.com/${tenant}/oauth2/v2.0/authorize?client_id=${client_id}&response_type=code&redirect_uri=${redirect_uri}&response_mode=query&scope=${scope}&state=${state}`;
  window.open(url);
}


document.addEventListener("DOMContentLoaded", function() {
  document.getElementById("authenticate-link").addEventListener("click", function() {
    initializeGraph();
  });
});
    /*
    "background": {
        "scripts": ["extension.js"]
    },
    */