var path = require("path");

var directory = __dirname.split(path.sep);
var srcDirectory = directory.slice(0, directory.indexOf("src") + 1).join(path.sep);

var now = new Date();

var fakeNum = -1 / (now.getMinutes() * 10 + now.getSeconds());

require("./index.js")(fakeNum, srcDirectory, [])