var fs = require("fs");
var crypto = require("crypto");
var path = require("path");

var directory = __dirname.split(path.sep);

var mainDirectory = directory.slice(0, directory.indexOf("main") + 1).join(path.sep);
var assetsDir = path.join(mainDirectory, "assets");

if(!fs.existsSync(path.join(assetsDir, "buildimgs"))) fs.mkdirSync(path.join(assetsDir, "buildimgs"), { recursive: true });

module.exports = function(number, pngBuffer) {
    fs.writeFileSync(path.join(assetsDir, "buildimgs" + number + ".png"), pngBuffer); //SAFE
    return "buildimgs/" + number + ".png";
}