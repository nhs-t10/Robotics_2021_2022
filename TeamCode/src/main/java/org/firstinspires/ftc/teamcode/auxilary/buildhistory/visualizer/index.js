var gitTools = require("./git-tools");

var gitRel = gitTools.gitRelativeFile(__dirname + "/../index.js");

(async function() {
    await gitTools.loadAllObjectsFromPack();

    //console.log(gitTools.getAllVersionsOfFile(gitRel));
})();