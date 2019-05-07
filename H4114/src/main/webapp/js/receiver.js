/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var socketListen;
var pcListen;
var listenName;



const configurationListen = {iceServers: [{urls: 'turn:10.43.2.185:3478',credential: 'test',
    username: 'test'}]};

function setLocalAndSendMessage(sessionDescription) {
    pcListen.setLocalDescription(sessionDescription);
    sendMessageListen(sessionDescription);
}

function errorMsg(msg, error) {
    const errorElement = document.querySelector('#errorMsg');
    errorElement.innerHTML += `<p>${msg}</p>`;
    if (typeof error !== 'undefined') {
      console.error(error);
    }
}

function connectListen(number, name){
    listenName = name;
    socketListen = new WebSocket("ws://192.168.137.1:8084/H4114/video/" + number + "/listen/" + name);//"wss://192.168.137.1:8443/H4114/video/" + number + "/listen/" + name
    socketListen.onopen = function (event) {
        console.log("/!\\ Connexion serveur");
    };
    socketListen.onerror = function (event) {
        console.log(event);
    };
    socketListen.onmessage = function (event) {
        errorMsg(event.data);
        console.log(event.data);
        var json = JSON.parse(event.data);
        if(json.type === 'candidate') {
            var candidate = new RTCIceCandidate(json.candidate);
            pcListen.addIceCandidate(candidate);
            console.log(candidate);
        }else if(json.type === 'offer') {
            console.log(json.sdp);
            handleVideoOfferMsg(json);
        }
    };
    socketListen.onclose = function (event) {
        sendMessageListen({
            user : 'start',
            type : 'bye'
        });
        console.log("/!\\ Déconnexion serveur");
    };
}

function handleVideoOfferMsg(msg) {
    createPeerConnectionListen();
    var desc = new RTCSessionDescription(msg.sdp);

    pcListen.setRemoteDescription(desc).then(function() {
        return pcListen.createAnswer();
    })
    .then(function(answer) {
        return pcListen.setLocalDescription(answer);
    })
    .then(function() {
        var msgSend = {
            user: "listen",
            type: "answer",
            name: listenName,
            sdp: pcListen.localDescription
        }; 
        errorMsg(msgSend);
        sendMessageListen(msgSend);
        console.log(pcListen);
    });
}


function createPeerConnectionListen() {
    pcListen = new RTCPeerConnection(configurationListen);

    pcListen.onicecandidate = handleICECandidateEventListen;
    pcListen.ontrack = handleTrackEventListen;
    pcListen.onnegotiationneeded = handleNegotiationNeededEventListen;
    pcListen.onremovetrack = handleRemoveTrackEventListen;
    pcListen.oniceconnectionstatechange = handleICEConnectionStateChangeEventListen;
    pcListen.onicegatheringstatechange = handleICEGatheringStateChangeEventListen;
    pcListen.onsignalingstatechange = handleSignalingStateChangeEventListen;
}

function handleNegotiationNeededEventListen() {
    console.log("Sending offer to peer.");
    pcListen.createOffer().then(function(offer) {
        return pcListen.setLocalDescription(offer);
    })
    .then(function() {
        sendMessageListen({
            user : 'start',
            type : 'offer',
            sdp : pcListen.localDescription
        });
    });
}

function handleICECandidateEventListen(event) {
    if (event.candidate) {
        sendMessageListen({
            user : "listen",
            type: "candidate",
            name : listenName,
            candidate: event.candidate
        });
    }
}

function handleICEConnectionStateChangeEventListen(event) {
    console.log("*** ICE connection state changed to " + pcListen.iceConnectionState);

    switch(pcListen.iceConnectionState) {
        case "closed":
        case "failed":
        case "disconnected":
            break;
    }
}

function handleSignalingStateChangeEventListen(event) {
    console.log("*** WebRTC signaling state changed to: " + pcListen.signalingState);
    switch(pcListen.signalingState) {
        case "closed":
            break;
    }
}

function handleICEGatheringStateChangeEventListen(event) {
    console.log("*** ICE gathering state changed to: " + pcListen.iceGatheringState);
}

function handleTrackEventListen(event) {
    const video = document.getElementById('video');
    console.log("ashoidiasipdfsapfopapasfjasfpasfopaspasfoafjoaspfosajasajsfajsfopasjfodjapsdjopasjdpoj");
    errorMsg("ashoidiasipdfsapfopapasfjasfpasfopaspasfoafjoaspfosajasajsfajsfopasjfodjapsdjopasjdpoj");
    video.srcObject = event.streams[0];
    console.log(event.streams.length);
    console.log(document.getElementById("video").srcObject);
    errorMsg(document.getElementById("video").srcObject.id);
}

function handleRemoveTrackEventListen(event) {
    var stream = document.getElementById("videoDest").srcObject;
    var trackList = stream.getTracks();

    if (trackList.length === 0) {
        closeVideoCall();
    }
}

function sendMessageListen(message){
    var msgString = JSON.stringify(message);
    console.log('message sent : ' + msgString);
    socketListen.send(msgString);
}