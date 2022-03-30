require("..").registerTransmutation({
    requires: [],
    id: "optimized-bytecode",
    type: "alias",
    aliasesTo: "get-syntax-tree tree-check* syntax-tree-to-bytecode bcoptim-* package-bytecode-for-java process-template write-to-output-file :*postprocess*"
});