var cp = require("child_process");
var fs = require("fs");

var allCommits = cp.execSync("git log --pretty=oneline -- genealogy/*")
    .toString()
    .split("\n")
    .map(x=>x.split(" ")[0]);

var objects = [];
var objectToCommitMap = {}

for(var i = 0; i < allCommits.length; i++) {
    var commitObjects = getReleventObjectsInCommit(allCommits[i])
    objects = objects.concat(commitObjects);
    commitObjects.forEach(x=>objectToCommitMap[x] = allCommits[i]);
}

objects = Array.from(new Set(objects));

var builds = {};

for(var i = 0; i < objects.length; i++) {
    getBuildsFromFileHash(objects[i], objectToCommitMap[objects[i]]);
}

fs.writeFileSync(__dirname + "/allBuilds.json", JSON.stringify(Object.values(builds), null, 2)); //SAFE

function getBuildsFromFileHash(hash, commit) {
    var file = cp.execSync(`git cat-file blob ${hash}`).toString();
    try {
        var parse = JSON.parse(file);
        for(var i = 0; i < parse.builds.length; i++) {
            var hash = parse.builds[i].buildHash;
            builds[hash] = parse.builds[i];

            builds[hash].buildNumber = parse.buildCount - ( parse.builds.length - (i + 1));
            builds[hash].cognomen = parse.cognomen;
            builds[hash].browserHash = parse.browser;
            builds[hash].commit = commit
        }
    } catch(e) {}
}

function getReleventObjectsInCommit(commitID) {
    try {
        var genealogy = "TeamCode/src/main/java/org/firstinspires/ftc/teamcode/auxilary/buildhistory/genealogy";
        var treeID = cp.execSync(`git rev-parse ${commitID} -- ${genealogy}`).toString().split("\n")[0].trim();

        var objects = cp.execSync(`git ls-tree -rt --full-tree ${treeID} ${genealogy}`)
            .toString()
            .trim()
            .split("\n")
            .filter(x=>
                x
                .split(" ")[1] == "blob")
            .map(x=>
                x
                .split(" ")[2]
                .split("\t")[0]
            );
        return objects;
    } catch(e) {
        return [];
    }
}