package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable;

public enum VariableType {
    INTEGER {
        @Override
        public String getName() {
            return "Integer";
        }
    },
    DOUBLE {
        @Override
        public String getName() {
            return "Double";
        }
    },
    STRING {
        @Override
        public String getName() {
            return "String";
        }
    };

    public abstract String getName();
}  