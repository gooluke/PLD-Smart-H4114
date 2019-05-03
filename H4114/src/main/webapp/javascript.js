$(document).ready(function(){
    $('#content').load("map.html");
 });

function changeToMap() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("map").classList.add("active");
    $('#content').load("map.html");
}

function changeToView() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("view").classList.add("active");
    $('#content').load("view.html");
}

function changeToStream() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("stream").classList.add("active");
    $('#content').load("stream.html");
}

function changeToVote() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("vote").classList.add("active");
    $('#content').load("vote.html");
}

function changeToAlert() {
    document.getElementsByClassName("active")[0].classList.remove("active");
    document.getElementById("alert").classList.add("active");
    $('#content').load("alert.html");
}


    