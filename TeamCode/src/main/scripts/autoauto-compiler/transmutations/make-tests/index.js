var path = require("path");

var maketest = require("./make-test");

require("..").registerTransmutation({
    requires: [],
    id: "make-tests",
    type: "codebase_postprocess",
    run: function(context, contexts) {
        var testDir = path.join(context.sourceRoot, "test/java/org/firstinspires/ftc/teamcode/unitTests/__testedautoauto");
        
        var testRecords = contexts.map(x=>({
            frontMatter: x.fileFrontmatter,
            className: x.resultBaseFileName.split(".")[0],
            package: x.inputs["get-result-package"]
        }));
        
        maketest(testRecords, testDir);
    }
});
