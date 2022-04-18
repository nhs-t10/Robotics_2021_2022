var makeJava = require("./make-java");

module.exports = function run(context) {
    context.output = makeJava(context.inputs["text-to-syntax-tree"]);
    context.status = "pass";
}