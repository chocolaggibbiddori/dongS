package com.dongs.settings;

import com.dongs.common.exception.InvalidExtensionException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

class YamlReader extends BufferedReader {

    YamlReader(String configPath) throws FileNotFoundException, InvalidExtensionException {
        super(new FileReader(configPath));
        checkExtension(configPath);
    }

    private void checkExtension(String configPath) throws InvalidExtensionException {
        String[] extensions = {".yml", ".yaml"};
        for (String extension : extensions)
            if (configPath.endsWith(extension)) return;

        throw new InvalidExtensionException("Configuration file must have '.yml' or '.yaml' extension");
    }

    Setting readSettings() {
        Tree tree = parseYamlToTree();
        parseTreeToSetting(tree);

        return Setting.getInstance();
    }

    private Tree parseYamlToTree() {
        final Tree tree = new Tree();

        try (Stream<String> lines = lines()) {
            lines
                    .filter(l -> !l.isBlank())
                    .filter(this::isNotAnnotation)
                    .map(this::removeAnnotation)
                    .forEach(tree::createNode);
        }

        return tree;
    }

    private boolean isNotAnnotation(String line) {
        return !line.startsWith("#");
    }

    private String removeAnnotation(String line) {
        int i = line.indexOf("#");
        return i == -1 ? line : line.substring(0, i);
    }

    private void parseTreeToSetting(Tree tree) {
        applyNodeToSetting(tree.root);
    }

    private void applyNodeToSetting(Tree.Node node) {
        if (node.value.isEmpty()) {
            node.children.forEach(this::applyNodeToSetting);
            return;
        }

        try {
            Class<?> parentClass = Class.forName("com.dongs.settings.%s".formatted(kebabToPascal(node.parent.key)));
            Object parentInstance = parentClass
                    .getDeclaredMethod("getInstance")
                    .invoke(null);
            String setterMethodName = "set" + kebabToPascal(node.key);
            Class<?> valueClass = getClassOf(node.value);
            Method setterMethod = parentClass.getDeclaredMethod(setterMethodName, valueClass);
            Object value = getValueOf(node.value, valueClass);
            setterMethod.invoke(parentInstance, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                 ClassNotFoundException ignored) {
        }
    }

    private String kebabToPascal(String kebab) {
        StringBuilder pascal = new StringBuilder();

        for (String word : kebab.split("-")) {
            pascal
                    .append(word.toUpperCase().charAt(0))
                    .append(word.substring(1));
        }

        return pascal.toString();
    }

    private Class<?> getClassOf(String value) {
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

    private Object getValueOf(String value, Class<?> valueType) {
        if (valueType.equals(boolean.class)) return Boolean.parseBoolean(value);
        else if (valueType.equals(int.class)) return Integer.parseInt(value);
        return value;
    }

    private static class Tree {

        final Node root = new Node(-1, "setting", "");
        Node finalNode = root;

        void createNode(String line) {
            String[] split = line.split(":");
            String keyOrigin = split[0];
            String key = keyOrigin.trim();
            String value;
            if (split.length == 1) value = "";
            else {
                String valueOrigin = split[1];
                value = valueOrigin.isBlank() ? "" : valueOrigin.substring(1).trim();
            }
            int indent = keyOrigin.length() - key.length();
            Node node = new Node(indent, key, value);

            Node finalNode = this.finalNode;
            if (indent > finalNode.indent) finalNode.addChild(node);
            else if (indent == finalNode.indent) finalNode.parent.addChild(node);
            else finalNode.parent.parent.addChild(node);

            this.finalNode = node;
        }

        private static class Node {

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

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Node node)) return false;
                return indent == node.indent && Objects.equals(parent, node.parent) && Objects.equals(key, node.key) && Objects.equals(value, node.value);
            }

            @Override
            public int hashCode() {
                return Objects.hash(parent, indent, key, value);
            }
        }
    }
}
