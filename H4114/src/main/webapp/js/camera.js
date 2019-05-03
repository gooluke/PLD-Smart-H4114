/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var socket;
var localStream;
var started = false;
var pc;



const configuration = {iceServers: [{urls: 'stun:stun.l.googke.com:19302'}]};
const constraints = window.constraints = {
  audio: false,
  video: true
};


function start(){
    const video = document.getElementById('video');
    // Get access to the camera!
    if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
        // Not adding `{ audio: true }` since we only want video now
        navigator.mediaDevices.getUserMedia(constraints)
        .then(function(stream) {
            //video.src = window.URL.createObjectURL(stream);
            video.srcObject = stream;
            localStream = stream;
            video.play();
        })
        .catch(function(error) {
            /* handle the error */
            if (error.name === 'ConstraintNotSatisfiedError') {
                let v = constraints.video;
                errorMsg(`The resolution ${v.width.exact}x${v.height.exact} px is not supported by your device.`);
            } else if (error.name === 'PermissionDeniedError') {
                errorMsg('Permissions have not been granted to use your camera and ' +
                'microphone, you need to allow the page access to your devices in ' +
                'order for the demo to work.');
            }
            errorMsg(`getUserMedia error: ${error.name}`, error);
        });
    }
}

function stop() {
    const video = document.getElementById('video');
    video.pause();
    video.srcObject = null;
    video.src = "";
    localStream.getTracks().forEach(function(track) {
        track.stop();
    });
}

function setLocalAndSendMessage(sessionDescription) {
    pc.setLocalDescription(sessionDescription);
    sendMessage(sessionDescription);
}

function errorMsg(msg, error) {
    const errorElement = document.querySelector('#errorMsg');
    errorElement.innerHTML += `<p>${msg}</p>`;
    if (typeof error !== 'undefined') {
      console.error(error);
    }
}

function connectStart(){
    socket = new WebSocket("ws://localhost:8084/H4114/video/10/start");
    socket.onopen = function (event) {
        console.log("/!\\ Connexion serveur");
    };
    socket.onerror = function (event) {
        console.log(event);
    };
    socket.onmessage = function (event) {
        console.log(event.data);
        var json = JSON.parse(event.data);
        if(json.type === 'answer') {
            console.log(json.sdp);
            var desc = new RTCSessionDescription(json.sdp);
            pc.setRemoteDescription(desc);
        }
    };
    socket.onclose = function (event) {
        sendMessage({
            user : 'start',
            type : 'bye'
        });
        console.log("/!\\ Déconnexion serveur");
    };
}

function stream(){
    if (!started && localStream) {
        console.log("Creating PeerConnection.");
        createPeerConnection();
        console.log("Adding local stream.");
        console.log(localStream);
        localStream.getTracks().forEach(track => pc.addTrack(track, localStream));
        started = true;
    }
}


function connectListen(){
    socket = new WebSocket("ws://localhost:8084/H4114/video/10/listen");
    socket.onopen = function (event) {
        console.log("/!\\ Connexion serveur");
    };
    socket.onerror = function (event) {
        console.log(event);
    };
    socket.onmessage = function (event) {
        errorMsg(event.data);
        console.log(event.data);
        var json = JSON.parse(event.data);
        if(json.type === 'candidate') {
            var candidate = new RTCIceCandidate(json.candidate);
            pc.addIceCandidate(candidate);
        }else if(json.type === 'offer') {
            console.log(json.sdp);
            handleVideoOfferMsg(json);
        }
    };
    socket.onclose = function (event) {
        sendMessage({
            user : 'start',
            type : 'bye'
        });
        console.log("/!\\ Déconnexion serveur");
    };
}

function handleVideoOfferMsg(msg) {
    errorMsg("handleVideoOfferMsg");
    createPeerConnection();
    errorMsg("handleVideoOfferMsg2");
    var desc = new RTCSessionDescription(msg.sdp);
    errorMsg("handleVideoOfferMsg4");

    pc.setRemoteDescription(desc).then(function() {
        return pc.createAnswer();
        errorMsg("handleVideoOfferMsg5");
    })
    .then(function(answer) {
        return pc.setLocalDescription(answer);
    })
    .then(function() {
        errorMsg("handleVideoOfferMsg3");
        var msg = {
            user: "listen",
            type: "answer",
            sdp: pc.localDescription
        }; 
        errorMsg(msg);
        sendMessage(msg);
    });
}


function createPeerConnection() {
    errorMsg("createPeerConnection");
    errorMsg(configuration);
    pc = new RTCPeerConnection(configuration);
    errorMsg("createPeerConnection2");

    pc.onicecandidate = handleICECandidateEvent;
    pc.ontrack = handleTrackEvent;
    pc.onnegotiationneeded = handleNegotiationNeededEvent;
    pc.onremovetrack = handleRemoveTrackEvent;
    pc.oniceconnectionstatechange = handleICEConnectionStateChangeEvent;
    pc.onicegatheringstatechange = handleICEGatheringStateChangeEvent;
    pc.onsignalingstatechange = handleSignalingStateChangeEvent;
}

function handleNegotiationNeededEvent() {
    console.log("Sending offer to peer.");
    pc.createOffer().then(function(offer) {
        return pc.setLocalDescription(offer);
    })
    .then(function() {
        sendMessage({
            user : 'start',
            type : 'offer',
            sdp : pc.localDescription
        });
    });
}

function handleICECandidateEvent(event) {
    if (event.candidate) {
        sendMessage({
            user : "start",
            type: "candidate",
            candidate: event.candidate
        });
    }
}

function handleICEConnectionStateChangeEvent(event) {
    console.log("*** ICE connection state changed to " + pc.iceConnectionState);

    switch(pc.iceConnectionState) {
        case "closed":
        case "failed":
        case "disconnected":
            break;
    }
}

function handleSignalingStateChangeEvent(event) {
    console.log("*** WebRTC signaling state changed to: " + pc.signalingState);
    switch(pc.signalingState) {
        case "closed":
            break;
    }
}

function handleICEGatheringStateChangeEvent(event) {
    console.log("*** ICE gathering state changed to: " + pc.iceGatheringState);
}

function handleTrackEvent(event) {
    const video = document.getElementById('video');
    console.log("ashoidiasipdfsapfopapasfjasfpasfopaspasfoafjoaspfosajasajsfajsfopasjfodjapsdjopasjdpoj");
    errorMsg("ashoidiasipdfsapfopapasfjasfpasfopaspasfoafjoaspfosajasajsfajsfopasjfodjapsdjopasjdpoj");
    video.srcObject = event.streams[0];
    console.log(event.streams.length);
    console.log(document.getElementById("video").srcObject);
    errorMsg(document.getElementById("video").srcObject.id);
}

function handleRemoveTrackEvent(event) {
    var stream = document.getElementById("videoDest").srcObject;
    var trackList = stream.getTracks();

    if (trackList.length === 0) {
        closeVideoCall();
    }
}

function sendMessage(message){
    var msgString = JSON.stringify(message);
    console.log('message sent : ' + msgString);
    socket.send(msgString);
}
