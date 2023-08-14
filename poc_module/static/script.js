function setName(name) {
    console.log("my name is " + name);
    document.getElementsByTagName("h1")[0].innerHTML="manulife";
}

function getFlutterData() {
//request flutter to getData
    getData.postMessage("static/data.json");
}

function setFlutterData(d){
//for flutter to call
    document.getElementsByTagName("p")[0].innerHTML=JSON.stringify(d);
}

function callAndroid()
{
    callNative.postMessage("static/data.json");
}

function callNativeBack(){

}


function callByNative(msg){
    console.log("js------------------")
    setInterval(function(){document.getElementsByTagName("h2")[0].innerHTML=JSON.stringify(msg);},1500);
    console.log(msg)
    return "callByNative JS return :"+new Date();
}