require("..").registerTransmutation({
    requires: [],
    id: "get-syntax-tree",
    type: "alias",
    aliasesTo: "source-file-text *text-check* text-to-syntax-tree pre-convert-tree-units"
});