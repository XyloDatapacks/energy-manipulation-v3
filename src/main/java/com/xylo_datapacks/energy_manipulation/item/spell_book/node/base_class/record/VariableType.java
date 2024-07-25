package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record;

public enum VariableType {
    INTEGER {
        @Override
        public String getName() {
            return "Integer";
        }

        @Override
        public Object CreateVariable() {
            return Integer.valueOf(0);
        }
    },
    DOUBLE {
        @Override
        public String getName() {
            return "Double";
        }

        @Override
        public Object CreateVariable() {
            return Double.valueOf(0.0);
        }
    },
    STRING {
        @Override
        public String getName() {
            return "String";
        }

        @Override
        public Object CreateVariable() {
            return "";
        }
    };

    public abstract String getName();
    
    public abstract Object CreateVariable();
}  