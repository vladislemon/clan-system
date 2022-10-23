function main() {

}

function getContent() {
    return document.getElementById('content');
}

function createDiv() {
    return document.createElement('div');
}

function doGetRequest(url, callback) {
    doRequest(url, 'GET', callback);
}

function doRequest(url, method, callback) {
    const xhr = new XMLHttpRequest()
    xhr.open(method, url);
    xhr.send();
    xhr.onload = () => {
        callback(xhr.status, xhr.response);
    }
}

main();