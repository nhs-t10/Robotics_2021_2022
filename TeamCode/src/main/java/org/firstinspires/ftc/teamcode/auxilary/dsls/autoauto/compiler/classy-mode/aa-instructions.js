module.exports = {
    NOOP: 0,
    PUSH_CONST: 1,
    JUMP: 2, //pop a number off the stack & jump to that INSTRUCTION.
    ENDLOOP: 3,
    POP: 4,
    DUP: 5,
    GET_PROP: 6,
    SET_PROP: 7,
    GET_VAR: 8,
    SET_VAR: 9,
    CALL_FUNC: 10, //takes 1 additional arg-- the amount of arguments to give to the function
    INSTANCEOF: 11,

    ADD: 12,
    SUBTRACT: 13,
    MULTIPLY: 14,
    DIVIDE: 15,
    MODULO: 16,
    
    LESS_THAN: 17,
    GREATER_THAN: 18,
    EQUAL_TO: 19,
    NOT_EQUAL_TO: 20,

    IF_NONZERO_JUMP: 21,

    PUSH_TIME_MS: 22,

    JUMP_STATICVALUE: 23, //jump to given instruction. Has one extra value-- a number, the absolute index of an INSTRUCTION.
    GET_VAR_STATIC: 24,
    SET_VAR_STATIC: 25,

    IF_NONZERO_JUMP_STATIC: 26,

    GREATER_THAN_EQUALS: 27,
    LESS_THAN_EQUALS: 28,

    ABSOLUTE_VALUE: 29,

    GET_PROP_STATIC: 30,
    SET_PROP_STATIC: 31,

    PUSH_EMPTY_TABLE_REF: 32,

    UPDATE_LOOP_START_STATE: 33,
    UPDATE_LOOP_START_STATE_STATIC: 34,
};