var path = require("path");
var fs = require("fs");
var crypto = require("crypto");

var directory = __dirname.split(path.sep);
var rootDirectory = directory.slice(0, directory.indexOf("TeamCode")).join(path.sep);
var managersDir = path.join(rootDirectory, "TeamCode/src/main/java/org/firstinspires/ftc/teamcode/managers");

var cache = require("../../cache");

var CACHE_VERSION = 9002 + Math.random();

var cacheManagers = cache.get("autoauto-managers", {});

if(cacheManagers.cacheVersion != CACHE_VERSION) cacheManagers = {};

var robotFunctionsDirectory = path.join(rootDirectory, "TeamCode/gen/org/firstinspires/ftc/teamcode/auxilary/dsls/autoauto/runtime/robotfunctions");
if(!fs.existsSync(robotFunctionsDirectory)) cacheManagers = {};

cacheManagers.cacheVersion = CACHE_VERSION;

var managerArgs = {};

var generateAaMethods = require("./parse-and-generate-aa-methods.js");
var deleteUnusedMethodClasses = require("./delete-unused-method-classes.js");

if (!fs.existsSync(managersDir)) throw "Managers directory `" + managersDir + "` doesn't exist";

var managers = loadManagersFromFolder(managersDir);

var methods = [];
for (var i = 0; i < managers.length; i++) {
    var manager = managers[i];
    var fileContent = fs.readFileSync(manager).toString();
    
    var sha = crypto.createHash("sha256").update(fileContent).digest("hex");
    
    if(cacheManagers[manager] === undefined) cacheManagers[manager] = {};
    
    if(cacheManagers[manager].javaSha === sha) {
        methods.push(cacheManagers[manager].methods);
    } else {
        cacheManagers[manager].javaSha = sha
        var preexistingNames = methods.map(x=>x[1].map(y=>y[0])).flat();
        
        var generated = generateAaMethods(fileContent, preexistingNames);

        if(generated) cacheManagers[manager].methods = generated;
        
        methods.push(cacheManagers[manager].methods);
    }
}

deleteUnusedMethodClasses(methods.map(x=>x[1].map(y=>y[1])).flat());

var robotFunctionLoaderAddress = path.join(rootDirectory, "TeamCode/gen/org/firstinspires/ftc/teamcode/auxilary/dsls/autoauto/runtime/RobotFunctionLoader.java");
if(!fs.existsSync(path.dirname(robotFunctionLoaderAddress))) fs.mkdirSync(path.dirname(robotFunctionLoaderAddress), { recursive: true });

var robotFunctionsTemplate = require("./make-robotfunctionloader.js");
var robotFunctionLoader = robotFunctionsTemplate(
    methods
    .map(x =>
        x[1].map(y =>
            ({ funcname: y[0], varname: "func_" + y[0], classname: y[1], manager: makeManagerName(x[0])})
        )).flat(),
    Object.entries(managerArgs),
);
fs.writeFileSync(robotFunctionLoaderAddress, robotFunctionLoader);

cache.save("autoauto-managers", cacheManagers);

function makeManagerName(name) {
    if(name == "") return "";
    
    if(managerArgs[name]) return managerArgs[name];
    
    var basename = name.split(".").slice(-1)[0].replace(/Manager$/, "");
    
    managerArgs[name] = "man" + basename;
    return managerArgs[name] || "";
}

function loadManagersFromFolder(folder) {
    let results = [];

    let folderContents = fs.readdirSync(folder, {
        withFileTypes: true
    });

    for (var i = 0; i < folderContents.length; i++) {
        let subfile = folderContents[i];

        if (subfile.isDirectory()) {
            results = results.concat(loadManagersFromFolder(path.resolve(folder, subfile.name)));
        } else if (subfile.isFile() && (subfile.name.endsWith("Manager.java") || subfile.name == "PaulMath.java")) {
            results.push(path.resolve(folder, subfile.name));
        }
    }

    return results;
}