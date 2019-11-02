var app = {
initialize: function() {
document.addEventListener('deviceready',this.onDeviceReady.bind(this), false);
},
onDeviceReady: function() {
console.log("received deviceready");
document.getElementById("hellobutton").addEventListener("click",
 this.printhello);
 },
 printhello: function() {
 console.log("in printhello");document.getElementById("hellop").innerHTML = "Hallo Welt!";
 },
 };
 app.initialize();