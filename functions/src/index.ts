import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import { QuerySnapshot } from '@google-cloud/firestore'; // This needs to be listed as a dependency

import {Group} from './group'

admin.initializeApp()

// Since this code will be running in the Cloud Functions enviornment
// we call initialize Firestore without any arguments because it
// detects authentication from the environment.
const firestore = admin.firestore();

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
export const helloWorld = functions.https.onRequest((request, response) => {
  response.send("Hello from Firebase!");
});

/* In progress

// Create a new function which is triggered when a new document, {uid},
// is created in Queue. This new document represents a queuedUser object
// and this function wil take the queuedUser and put them into a group.
export const addQueuedUserToGroup = functions.firestore
  .document('Queue/{uid}')
  .onCreate((snap, context) => {

    // First we want to get the groups that have a status of "waiting".
    const waitingGroupsQuery = firestore.collection('Group')
      .where('status', '==', 'waiting')
      .orderBy('lastCreated');
    
    waitingGroupsQuery.get().then(querySnapshot => {
      querySnapshot.forEach(docSnapshot => {
        console.log(`Found a waitingGroup doc at ${docSnapshot.ref.path}`);
        const waitingGroup = docSnapshot.data();
        for (var i = 0; i < waitingGroup.groupSizePrefs.size(); ++i) {
          if (pref && pref == queuedUser.groupSizePrefs)
        }
      })
    })
      const queueInfo = firestore.doc(`Queue\Info`).get();

    // I only need the info if I need the nextGid.
    // The get here returns a promise that contains the docSnap.
    firestore.collection(`Group`).doc(`Info`).get()
      .then(groupInfoSnap => {
        const groupInfo = groupInfoSnap.data();
        groupInfo.nextGid
      })


      const queuedUser = snap.data();

      // access a particular field as you would any JS property
      //const groupSizePrefs = queuedUser.groupSizePreferences;
      //console.log('Comparing prefs', context.params.documentId, queuedUser);
      
      //for (const pref in queuedUser.groupSizePreferences) {
        //if (pref && pref == group.groupSizePreferences) {
            
        //}
      //}
      //const GroupeneralDocRef = firestore.doc(`Group/${context.params.userId}`);

      // Create a new group
      return snap.ref.set({Group}, {merge: true});
      
    });

//In progress END
*/


/*
// SAMPLE CODE, for reference only!
// Listens for new messages added to /messages/:documentId/original and creates an
// uppercase version of the message to /messages/:documentId/uppercase
// [START makeUppercaseTrigger]
exports.makeUppercase = functions.firestore.document('/messages/{documentId}')
.onCreate((snap, context) => {
// [END makeUppercaseTrigger]
  // [START makeUppercaseBody]
  // Grab the current value of what was written to the Realtime Database.
  const original = snap.data().original;
  console.log('Uppercasing', context.params.documentId, original);
  const uppercase = original.toUpperCase();
  // You must return a Promise when performing asynchronous tasks inside a Functions such as
  // writing to the Firebase Realtime Database.
  // Setting an 'uppercase' sibling in the Realtime Database returns a Promise.
  return snap.ref.set({uppercase}, {merge: true});
  // [END makeUppercaseBody]

});
  */