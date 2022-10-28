function main() {
    loadUsers();
    document.getElementById('users_button').onclick = (event) => loadUsers();
    document.getElementById('clans_button').onclick = (event) => loadClans();
    document.getElementById('tasks_button').onclick = (event) => loadTasks();
    document.getElementById('transactions_button').onclick = (event) => loadGoldTransactions();
}

function loadUsers() {
    doGetRequest("http://localhost:4567/api/users", onUsersLoadCallback);
}

function loadClans() {
    doGetRequest("http://localhost:4567/api/clans", onClansLoadCallback);
}

function loadTasks() {
    doGetRequest("http://localhost:4567/api/tasks", onTasksLoadCallback);
}

function loadGoldTransactions() {
    doGetRequest("http://localhost:4567/api/transactions/gold", onGoldTransactionsLoadCallback);
}

function onUsersLoadCallback(status, response) {
    const users = JSON.parse(response).map(user => createUserDiv(user));
    const newContent = createDiv();
    newContent.append(createP());
    newContent.append(document.createTextNode("Users:"));
    newContent.append(createP());
    newContent.append(...users);
    replaceContent(newContent);
}

function onClansLoadCallback(status, response) {
    const clans = JSON.parse(response).map(clan => createClanDiv(clan));
    const newContent = createDiv();
    newContent.append(createP());
    newContent.append(document.createTextNode("Clans:"));
    newContent.append(createP());
    newContent.append(...clans);
    replaceContent(newContent);
}

function onTasksLoadCallback(status, response) {
    const tasks = JSON.parse(response).map(task => createTaskDiv(task));
    const newContent = createDiv();
    newContent.append(createP());
    newContent.append(document.createTextNode("Tasks:"));
    newContent.append(createP());
    newContent.append(...tasks);
    replaceContent(newContent);
}

function createUserDiv(user) {
    const userDiv = createDiv();
    userDiv.className = "user";

    const name = document.createTextNode(user.name + " [" + user.gold + " gold]");
    userDiv.append(name);

    return userDiv;
}

function createClanDiv(clan) {
    const clanDiv = createDiv();
    clanDiv.className = "clan";

    const name = document.createTextNode(clan.name + " [" + clan.gold + " gold]");
    clanDiv.append(name);

    return clanDiv;
}

function createTaskDiv(task) {
    const taskDiv = createDiv();
    taskDiv.className = "task";

    const name = document.createTextNode(task.goldReward + " gold reward");
    taskDiv.append(name);

    return taskDiv;
}

function replaceContent(newContent) {
    const oldContent = getContent();
    newContent.id = oldContent.id;
    oldContent.replaceWith(newContent);
}

function getContent() {
    return document.getElementById('content');
}

function createDiv() {
    return document.createElement('div');
}

function createP() {
    return document.createElement('p');
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