// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

// <ProgramSnippet>
const readline = require('readline-sync');

const settings = require('./appSettings');
const graphHelper = require('./graphHelper');

async function main() {
  console.log('JavaScript Graph Tutorial');

  let choice = 0;

  // Initialize Graph
  initializeGraph(settings);

  // Greet the user by name
  await greetUserAsync();

  const choices = [
    'Display access token',
    'List my inbox',
    'Send mail',
    'Make a Graph call'
  ];

  while (choice != -1) {
    choice = readline.keyInSelect(choices, 'Select an option', { cancel: 'Exit' });

    switch (choice) {
      case -1:
        // Exit
        console.log('Goodbye...');
        break;
      case 0:
        // Display access token
        await displayAccessTokenAsync();
        break;
      case 1:
        // List emails from user's inbox
        await listInboxAsync();
        break;
      case 2:
        // Send an email message
        await sendMailAsync();
        break;
      case 3:
        // Run any Graph code
        await makeGraphCallAsync();
        break;
      default:
        console.log('Invalid choice! Please try again.');
    }
  }
}

main();
// </ProgramSnippet>

// <InitializeGraphSnippet>
function initializeGraph(settings) {
  graphHelper.initializeGraphForUserAuth(settings, (info) => {
    // Display the device code message to
    // the user. This tells them
    // where to go to sign in and provides the
    // code to use.
    console.log(info.message);
  });
}
// </InitializeGraphSnippet>

// <GreetUserSnippet>
async function greetUserAsync() {
  try {
    const user = await graphHelper.getUserAsync();
    console.log(`Hello, ${user?.displayName}!`);
    // For Work/school accounts, email is in mail property
    // Personal accounts, email is in userPrincipalName
    console.log(`Email: ${user?.mail ?? user?.userPrincipalName ?? ''}`);
  } catch (err) {
    console.log(`Error getting user: ${err}`);
  }
}
// </GreetUserSnippet>

// <DisplayAccessTokenSnippet>
async function displayAccessTokenAsync() {
  try {
    const userToken = await graphHelper.getUserTokenAsync();
    console.log(`User token: ${userToken}`);
  } catch (err) {
    console.log(`Error getting user access token: ${err}`);
  }
}
// </DisplayAccessTokenSnippet>

// <ListInboxSnippet>
async function listInboxAsync() {
  try {
    const messagePage = await graphHelper.getInboxAsync();
    const messages = messagePage.value;

    // Output each message's details
    for (const message of messages) {
      console.log(`Message: ${message.subject ?? 'NO SUBJECT'}`);
      console.log(`  From: ${message.from?.emailAddress?.name ?? 'UNKNOWN'}`);
      console.log(`  Status: ${message.isRead ? 'Read' : 'Unread'}`);
      console.log(`  Received: ${message.receivedDateTime}`);
    }

    // If @odata.nextLink is not undefined, there are more messages
    // available on the server
    const moreAvailable = messagePage['@odata.nextLink'] != undefined;
    console.log(`\nMore messages available? ${moreAvailable}`);
  } catch (err) {
    console.log(`Error getting user's inbox: ${err}`);
  }
}
// </ListInboxSnippet>

// <SendMailSnippet>
async function sendMailAsync() {
  try {
    // Send mail to the signed-in user
    // Get the user for their email address
    const user = await graphHelper.getUserAsync();
    const userEmail = user?.mail ?? user?.userPrincipalName;

    if (!userEmail) {
      console.log('Couldn\'t get your email address, canceling...');
      return;
    }

    await graphHelper.sendMailAsync('Testing Microsoft Graph',
      'Hello world!', userEmail);
    console.log('Mail sent.');
  } catch (err) {
    console.log(`Error sending mail: ${err}`);
  }
}
// </SendMailSnippet>

// <MakeGraphCallSnippet>
async function makeGraphCallAsync() {
  const testId = 'AQQkADAwATM0MDAAMS0xZjc2LWVlADI0LTAwAi0wMAoAEABK5vbEtUItT5jLSEvk0N5j';
  try {
    const messagePage = await graphHelper.getConversationId();
    const messages = messagePage.value;

    let messageId = '';
    // Output each message's details
    for (const message of messages) {
      console.log(`conversation id : ${message.conversationId}`);
      if(message.conversationId == testId){
        messageId = message.id;
        break;
      }
    }
    const headers = await graphHelper.getInternetMessageHeaders(messageId);
    let headersToEvaluate = {};
    for(const h of headers.internetMessageHeaders){
      if(h.name == 'Authentication-Results' ){
        headersToEvaluate.authentication = h.value;
        console.log(`${h.name} : ${h.value}`);
      }
      if(h.name == 'From' ){
        headersToEvaluate.from = h.value;
        console.log(`${h.name} : ${h.value}`);
      }
      if(h.name == 'Return-Path' ){
        headersToEvaluate.returnpath = h.value;
        console.log(`${h.name} : ${h.value}`);
      }
    }

    const startIndex = headersToEvaluate.from.indexOf('<');
    const endIndex = headersToEvaluate.from.indexOf('>');
    // Extract the substring between '<' and '>'
    const substring = headersToEvaluate.from.substring(startIndex + 1, endIndex);

    if(substring == headersToEvaluate.returnpath){
      console.log('From and Return-Path match');
    }else{
      console.log(`from ${substring} doesnt match ${headersToEvaluate.returnpath}`);
    }

    if( headersToEvaluate.authentication.includes('spf=pass')){
      console.log('Passed spf authentication');
    }else{
      console.log('Did not pass spf authentication');
    }
    if(headersToEvaluate.authentication.includes('dkim=pass')){
      console.log('Passed dkim authentication');
    }else{
      console.log('Did not pass dkim authentication');
    }
    if(headersToEvaluate.authentication.includes('dmarc=pass')){
      console.log('Passed dmarc authentication');
    }else{
      console.log('Did not pass dmarc authentication');
    }




    // If @odata.nextLink is not undefined, there are more messages
    // available on the server
    const moreAvailable = messagePage['@odata.nextLink'] != undefined;
    console.log(`\nMore messages available? ${moreAvailable}`);
  } catch (err) {
    console.log(`Error making Graph call: ${err}`);
  }
}
// </MakeGraphCallSnippet>
