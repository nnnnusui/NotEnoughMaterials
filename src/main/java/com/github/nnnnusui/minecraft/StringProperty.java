package com.github.nnnnusui.minecraft;

import com.google.common.collect.ImmutableSet;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.state.Property;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StringProperty extends Property<String>{
    private final List<String> allowedValues;

    protected StringProperty(String name, Collection<String> _allowedValues){
        super(name, String.class);
        if (_allowedValues.isEmpty())
            throw new IllegalArgumentException("allowedValues must not be empty");
        allowedValues = new ImmutableSet.Builder<String>().addAll(_allowedValues).build().asList();
    }
    public static StringProperty create(String name, Collection<String> _allowedValues)
        { return new StringProperty(name, _allowedValues); }
    @Override public Collection<String> getAllowedValues() { return allowedValues; }
    @Override public String getName(String value) { return value; }
    @Override public Optional<String> parseValue(String value) {
        int index = allowedValues.indexOf(value);
        if (index == -1) return Optional.empty();
        return Optional.of(allowedValues.get(index));
    }
    @Override public int computeHashCode() {
        return 31 * super.computeHashCode() + this.allowedValues.hashCode();
    }
}
