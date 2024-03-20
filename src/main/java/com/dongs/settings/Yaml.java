package com.dongs.settings;

import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Log
public class Yaml {

    private Yaml() {
    }

    public static Setting readSettings(String configPath) throws InvalidExtensionException, FileNotFoundException {
        // TODO [2024-03-21]: Required test
        Objects.requireNonNull(configPath, "configPath is null");
        checkExtension(configPath);

        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            parseYamlToNodeTree(reader);
            parseNodeTreeToSetting(Node.ROOT);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (IOException e) {
            log.warning(e.getMessage());
        }

        return Setting.getInstance();
    }

    private static void checkExtension(String configPath) throws InvalidExtensionException {
        String[] extensions = {".yml", ".yaml"};
        for (String extension : extensions)
            if (configPath.endsWith(extension)) return;

        throw new InvalidExtensionException("Configuration file must have '.yml' or '.yaml' extension");
    }

    private static void parseYamlToNodeTree(BufferedReader reader) {
        reader.lines()
                .filter(Yaml::isNotAnnotation)
                .map(Yaml::removeAnnotation)
                .forEach(Yaml::createNodeTree);
    }

    private static boolean isNotAnnotation(String s) {
        return !s.startsWith("#");
    }

    private static String removeAnnotation(String s) {
        int i = s.indexOf("#");
        return s.substring(0, i);
    }

    private static void createNodeTree(String s) {
        String[] split = s.split(":");
        String keyOrigin = split[0];
        String valueOrigin = split[1];

        String key = keyOrigin.trim();
        String value = valueOrigin.isBlank() ? "" : valueOrigin.substring(1).trim();
        int indent = keyOrigin.length() - key.length();
        Node node = new Node(indent, key, value);

        Node finalNode = Node.finalNode;
        if (indent > finalNode.indent) finalNode.addChild(node);
        else if (indent == finalNode.indent) finalNode.parent.addChild(node);
        else finalNode.parent.parent.addChild(node);

        Node.finalNode = node;
    }

    private static void parseNodeTreeToSetting(Node node) {
        if (node.value.isEmpty()) {
            node.children.forEach(Yaml::parseNodeTreeToSetting);
            return;
        }

        try {
            Class<?> parentClass = Class.forName(kebabToPascal(node.parent.key));
            Object parentInstance = parentClass
                    .getDeclaredMethod("getInstance")
                    .invoke(null);
            String setterMethodName = "set" + kebabToPascal(node.key);
            Class<?> valueClass = getValueClass(node.value);
            Method setterMethod = parentClass.getDeclaredMethod(setterMethodName, valueClass);
            setterMethod.invoke(parentInstance, node.value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException ignored) {
        }
    }

    private static String kebabToPascal(String kebab) {
        StringBuilder pascal = new StringBuilder();

        for (String word : kebab.split("-")) {
            pascal
                    .append(word.toUpperCase().charAt(0))
                    .append(word.substring(1));
        }

        return pascal.toString();
    }

    private static Class<?> getValueClass(String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return boolean.class;
        }

        try {
            Integer.parseInt(value);
            return int.class;
        } catch (NumberFormatException ignored) {
        }

        return String.class;
    }

    private static class Node {

        static final Node ROOT = new Node(-1, "setting", "");
        static Node finalNode = ROOT;

        Node parent;
        Set<Node> children = new HashSet<>();
        int indent;
        String key;
        String value;

        Node(int indent, String key, String value) {
            this.indent = indent;
            this.key = key;
            this.value = value;
        }

        void addChild(Node child) {
            child.parent = this;
            children.add(child);
        }
    }
}
