package com.example.sweater_letscode.spring.mapper;

public interface MyCustomMapper<F, T> {
    T map (F f);
}
