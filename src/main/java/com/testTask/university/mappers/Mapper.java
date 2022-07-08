package com.testTask.university.mappers;

public interface Mapper<T, E> {
    T convertToDto(E e);
    E convertToEntity(T t);
}
