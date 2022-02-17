require("..").registerTransmutation({
    requires: [],
    id: "optimized-bytecode",
    type: "alias",
    aliasesTo: "get-syntax-tree tree-check* syntax-tree-to-bytecode bcoptim-* process-template write-to-output-file :*postprocess*"
});