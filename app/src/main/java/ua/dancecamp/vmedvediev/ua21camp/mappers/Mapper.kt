package ua.dancecamp.vmedvediev.ua21camp.mappers

interface Mapper<T: Any, R: Any> {

    fun from(initialObject: T) : R

    fun to(initialObject: R) : T
}