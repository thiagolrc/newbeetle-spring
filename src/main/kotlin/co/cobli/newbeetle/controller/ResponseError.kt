package co.cobli.newbeetle.controller

class ResponseError<T>(val errors: Map<String, String>) : Response<T>
