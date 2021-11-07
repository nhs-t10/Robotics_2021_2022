var path = require("path");

var directory = __dirname.split(path.sep);
var srcDirectory = directory.slice(0, directory.indexOf("src") + 1).join(path.sep);

require("./index.js")(0, srcDirectory, [])