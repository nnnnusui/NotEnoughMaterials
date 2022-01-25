package com.github.nnnnusui.minecraft;

import com.google.common.collect.ImmutableSet;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StringProperty extends Property<String> {
    private final List<String> possibleValues;

    protected StringProperty(String name, Collection<String> _possibleValues){
        super(name, String.class);
        if (_possibleValues.isEmpty())
            throw new IllegalArgumentException("possibleValues must not be empty");
        this.possibleValues = new ImmutableSet.Builder<String>().addAll(_possibleValues).build().asList();
    }
    public static StringProperty create(String name, Collection<String> _possibleValues) {
        return new StringProperty(name, _possibleValues);
    }
    @Override
    public Collection<String> getPossibleValues() {
        return possibleValues;
    }

    @Override public String getName(String value) { return value; }

    @Override
    public Optional<String> getValue(String value) {
        int index = possibleValues.indexOf(value);
        if (index == -1) return Optional.empty();
        return Optional.of(possibleValues.get(index));
    }

    public int generateHashCode() {
        return 31 * super.generateHashCode() + this.possibleValues.hashCode();
    }
}
