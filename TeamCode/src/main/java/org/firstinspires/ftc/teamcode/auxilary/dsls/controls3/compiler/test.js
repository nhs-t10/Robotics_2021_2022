var util = require('util');

var fs = require("fs");
var parser = require("./parser");
const astTools = require('./ast-tools');

var file = fs.readFileSync(__dirname + "/test.c3").toString();
var parsed = parser(file);

fs.writeFileSync(__dirname + "/test.json", JSON.stringify(parsed, null, 2))

var source = astTools(parsed);

fs.writeFileSync(__dirname + "/test.java", source);
