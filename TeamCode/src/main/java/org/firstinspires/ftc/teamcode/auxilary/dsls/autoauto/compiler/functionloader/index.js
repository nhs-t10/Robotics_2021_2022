var path = require("path");
var fs = require("fs");
var crypto = require("crypto");

var directory = __dirname.split(path.sep);
var rootDirectory = directory.slice(0, directory.indexOf("TeamCode")).join(path.sep);
var managersDir = path.join(rootDirectory, "TeamCode/src/main/java/org/firstinspires/ftc/teamcode/managers");

var cacheDir = path.join(__dirname, ".cache");
if(!fs.existsSync(cacheDir)) fs.mkdirSync(cacheDir);

var cacheFile = path.join(__dirname, ".cache/managers.json");
if(!fs.existsSync(cacheFile)) fs.writeFileSync(cacheFile, "{}");
var cacheManagers = require(cacheFile);

var managerArgs = {};

var generateAaMethods = require("./parse-and-generate-aa-methods.js");

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
        
        cacheManagers[manager].methods = generateAaMethods(fileContent, preexistingNames);
        
        methods.push(cacheManagers[manager].methods);
    }
}


var robotFunctionLoaderAddress = path.join(rootDirectory, "TeamCode/src/main/java/org/firstinspires/ftc/teamcode/auxilary/dsls/autoauto/runtime/RobotFunctionLoader.java");
var robotFunctionsTemplate = require("./render-rfl.js");
var robotFunctionLoader = robotFunctionsTemplate(
    methods
    .map(x =>
        x[1].map(y =>
            `scope.put("${y[0]}", new ${y[1]}(${makeManagerName(x[0])}));`
        )
            .join("\n")
            + "\n"
    )
    .join("\n"),
    
    Object.entries(managerArgs)
);
fs.writeFileSync(robotFunctionLoaderAddress, robotFunctionLoader);

fs.writeFileSync(cacheFile, JSON.stringify(cacheManagers));

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