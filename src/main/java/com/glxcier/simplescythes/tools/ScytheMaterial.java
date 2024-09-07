package com.glxcier.simplescythes.tools;

public enum ScytheMaterial {
    WOODEN("Wooden"),
    STONE("Stone"),
    GOLDEN("Golden"),
    IRON("Iron"),
    DIAMOND("Diamond"),
    NETHERITE("Netherite");

    private final String name;

    ScytheMaterial(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ScytheMaterial fromString(String string) {
        for (ScytheMaterial material : ScytheMaterial.values()) {
            if (material.getName().equalsIgnoreCase(string)) {
                return material;
            }
        }
        return null;
    }
}
