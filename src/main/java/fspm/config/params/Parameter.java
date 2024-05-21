package fspm.config.params;

import java.rmi.UnexpectedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import fspm.util.KeyElement;
import fspm.util.exceptions.KeyNotFoundException;
import fspm.util.exceptions.TypeNotFoundException;

/**
 * Parameter represents variables that can be stored using Java data types or other user-defined
 * types.
 * <p>
 * The value field should be implemented by extending members, as values require specific primitive
 * type declarations.
 * 
 * @author Ou-An Chuang
 */
public class Parameter extends KeyElement {

    private JsonNode node;

    /**
     * @param key The parameter key.
     * @throws UnexpectedException
     */
    public Parameter(String key, JsonNode node) throws UnexpectedException {
        super(key);

        if (node.isObject()) {
            throw new UnexpectedException(
                    "Cannot store an object as a parameter.");
        } else {
            this.node = node;
        }
    }

    // @SuppressWarnings("unchecked")
    // public <T> T getValue() {
    // try {
    // if (node.isTextual()) {
    // return (T) node.asText();
    // } else if (node.isDouble()) {
    // return (T) Double.valueOf(node.asDouble());
    // } else if (node.isInt()) {
    // return (T) Integer.valueOf(node.asInt());
    // } else if (node.isBoolean()) {
    // return (T) Boolean.valueOf(node.asBoolean());
    // } else if (node.isArray()) {
    // ObjectMapper objectMapper = new ObjectMapper();
    // return (T) objectMapper.treeToValue(node, ArrayList.class);
    // }
    // } catch (Exception e) {
    // StringWriter sw = new StringWriter();
    // e.printStackTrace(new PrintWriter(sw));
    // throw new RuntimeException(
    // String.format("An error occurred while parsing parameter: %s.\n%s",
    // super.getKey(), sw));
    // }
    // throw new UnsupportedOperationException(super.getKey() + " uses an
    // unsupported type.");
    // }

    public JsonNodeType getType() {
        return node.getNodeType();
    }

    public Boolean asBoolean() {
        if (isNull()) {
            return null;
        }
        if (node.isBoolean()) {
            return node.asBoolean();
        }
        throw new KeyNotFoundException(super.getKey());
    }

    public String asString() {
        if (isNull()) {
            return null;
        }
        if (node.isTextual()) {
            return node.asText();
        }
        throw new KeyNotFoundException(super.getKey());
    }

    public Integer asInteger() {
        if (isNull()) {
            return null;
        }
        if (node.isInt()) {
            return node.asInt();
        }
        throw new KeyNotFoundException(super.getKey());
    }

    public Double asDouble() {
        if (isNull()) {
            return null;
        }
        Class<?> type = getDoubleType(node);

        if (type.equals(Double.class)) {
            return node.asDouble();
        } else if (type.equals(Integer.class)) {
            return (double) node.asInt();
        } else if (type.equals(Float.class)) {
            String value = node.asText();
            return (double) Float.parseFloat(value);
        }
        throw new KeyNotFoundException(super.getKey());
    }

    public Boolean[] asBooleanArray() {
        return asArray(Boolean[].class);
    }

    public String[] asStringArray() {
        return asArray(String[].class);
    }

    public Integer[] asIntegerArray() {
        return asArray(Integer[].class);
    }

    public Double[] asDoubleArray() {
        return asArray(Double[].class);
    }

    public <T> T[] asArray(Class<T[]> type) {
        if (isNull()) {
            return null;
        }
        if (type == null) {
            throw new RuntimeException("Array type should not be null.");
        }
        if (!node.isArray()) {
            throw new RuntimeException(getKey() + " is not an array.");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode firstItem = node.get(0);

            // Check if first item is the given type
            // Note: only checks first item as arrays with multiple types will be considered as invalid JSON
            if (type.equals(Boolean[].class) && firstItem.isBoolean()
                    || type.equals(String[].class) && firstItem.isTextual()
                    || type.equals(Integer[].class) && firstItem.isInt()
                    || type.equals(Double[].class)
                            && getDoubleType(firstItem) != null) {
                // Note: treeToValue also converts integer and string items to double.
                return objectMapper.treeToValue(node, type);
            } else {
                // Does not correspond to any valid type
                throw new TypeNotFoundException(getKey(), type.getSimpleName(),
                        "Array should consist of booleans, strings, integers or doubles.");
            }
        } catch (JsonProcessingException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(String.format(
                    "Error in converting '%s' to an array of type '%s'."
                            + " Note that an array containing multiple types is invalid.\n"
                            + e.toString(),
                    super.getKey(), type.getSimpleName()));
        }
    }

    private Class<?> getDoubleType(JsonNode node) {
        if (node.isDouble()) {
            return Double.class;
        } else if (node.isInt()) {
            // Case where double parameters are written as integers, e.g: "1.0" becomes "1"
            return Integer.class;
        } else if (node.isTextual() && node.asText().endsWith("f")) {
            // Case where floats are stored as strings, e.g: "10.5f"
            return Float.class;
        }
        return null;
    }

    public boolean isNull() {
        return node.isNull();
    }

    @Override
    public String toString() {
        return super.getKey() + ": " + node.asText();
    }
}
