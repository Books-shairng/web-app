package com.ninjabooks.util;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public final class CommonUtils
{
    private CommonUtils() {
    }

    @SafeVarargs
    public static <T> Supplier<Stream<T>> asSupplier(T... values) {
        return () -> Stream.of(values);
    }

    public static <T> Supplier<Stream<T>> asEmptySupplier() {
        return Stream::empty;
    }

    public static <T> Optional<T> asOptional(T value) {
        return Optional.ofNullable(value);
    }

    @SuppressWarnings("unchecked")
    public static <T, C extends Collection> List<T> domainObjectAsDto(C collection, Class<T> clazz) {
        final ModelMapper modelMapper = new ModelMapper();
        return (List<T>) collection.parallelStream()
            .map(o -> modelMapper.map(o, clazz))
            .collect(Collectors.toList());

    }
}
