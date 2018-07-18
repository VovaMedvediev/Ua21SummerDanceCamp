package com.example.vmedvediev.ua21summerdancecamp.mappers

interface Mapper<T: Any, R: Any> {

    fun map(initialObject: T) : R

}