package com.example.sweater_letscode.spring.querydsl;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicates {
    private final ArrayList<com.querydsl.core.types.Predicate> predicates = new ArrayList<>();

    public static QPredicates builder(){
        return new QPredicates();
    }

    public <T> QPredicates add(T object, Function<T, com.querydsl.core.types.Predicate> function){
        if(object!=null){
            predicates.add(function.apply(object));
        }
        return this;
    }


    public Predicate build(){
        return ExpressionUtils.allOf(predicates);

    }



}
