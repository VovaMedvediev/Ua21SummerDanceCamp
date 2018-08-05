package com.example.vmedvediev.ua21summerdancecamp.mappers

interface Mapper<T: Any, R: Any> {

    fun from(initialObject: T, isEvent: Boolean) : R

    fun to(initialObject: R) : T
}