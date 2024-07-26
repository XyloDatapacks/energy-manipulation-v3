package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record;

import net.minecraft.nbt.*;

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

        @Override
        public NbtElement toNbt(Object variable) {
            if (variable instanceof Integer var) {
                return NbtInt.of(var);
            }
            return null;
        }

        @Override
        public Object fromNbt(NbtElement nbtElement) {
            if (nbtElement instanceof NbtInt var) {
                return var.intValue();
            }
            return null;
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

        @Override
        public NbtElement toNbt(Object variable) {
            if (variable instanceof Double var) {
                return NbtDouble.of(var);
            }
            return null;
        }

        @Override
        public Object fromNbt(NbtElement nbtElement) {
            if (nbtElement instanceof NbtDouble var) {
                return var.doubleValue();
            }
            return null;
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

        @Override
        public NbtElement toNbt(Object variable) {
            if (variable instanceof String var) {
                return NbtString.of(var);
            }
            return null;
        }

        @Override
        public Object fromNbt(NbtElement nbtElement) {
            if (nbtElement instanceof NbtString var) {
                return var.asString();
            }
            return null;
        }
    };

    public abstract String getName();
    
    public abstract Object CreateVariable();

    public abstract NbtElement toNbt(Object variable);
    public abstract Object fromNbt(NbtElement nbtElement);

    
    public static final VariableType[] variableTypes = VariableType.values();
    
    public static NbtElement writeToNbt(Object variable) {
        for (VariableType type : variableTypes) {
            NbtElement result = type.toNbt(variable);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    public static Object readFromNbt(NbtElement nbtElement) {
        for (VariableType type : variableTypes) {
            Object result = type.fromNbt(nbtElement);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    
}  