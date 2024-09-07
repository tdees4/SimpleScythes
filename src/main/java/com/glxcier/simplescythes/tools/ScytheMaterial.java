package com.glxcier.simplescythes.tools;

/**
 * Enumeration that contains the different materials that a Scythe
 * can be created from.
 */
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

    /**
     * @return The ScytheMaterial in String form.
     */
    public String getName() {
        return name;
    }

    /**
     * Takes a string parameter and returns a ScytheMaterial enum that
     * matches it. For example, "WOODEN" would return ScytheMaterial.WOODEN.
     * @param string The string to be converted.
     * @return The matching ScytheMaterial.
     */
    public static ScytheMaterial fromString(String string) {
        for (ScytheMaterial material : ScytheMaterial.values()) {
            if (material.getName().equalsIgnoreCase(string)) {
                return material;
            }
        }
        return null;
    }
}
