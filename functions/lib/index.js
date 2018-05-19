"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const functions = require("firebase-functions");
// 'use strict';
// const functions = require('firebase-functions');
// const sanitizer = require('./sanitizer');
// const admin = require('firebase-admin');
// admin.initializeApp(functions.config().firebase);
// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript
exports.helloWorld = functions.https.onRequest((request, response) => {
    console.log("helloWorld function says hello!");
    response.send("Hello from DontDineAlone's Firebase!");
});
//# sourceMappingURL=index.js.map