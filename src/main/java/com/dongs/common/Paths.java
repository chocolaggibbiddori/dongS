package com.dongs.common;

public class Paths {

    public static final String MAIN_RESOURCES_ROOT = "src/main/resources/";
    public static final String TEST_RESOURCES_ROOT = "src/test/resources/";

    private static final String MAIN_RESOURCES_FILE = "src/main/resources/%s%s%s";
    private static final String TEST_RESOURCES_FILE = "src/test/resources/%s%s%s";

    private Paths() {
    }

    public static String getResourcesPathInMain(String directory, String filename, String extension) {
        return MAIN_RESOURCES_FILE.formatted(directory, filename, extension);
    }

    public static String getResourcesPathInTest(String directory, String filename, String extension) {
        return TEST_RESOURCES_FILE.formatted(directory, filename, extension);
    }
}
