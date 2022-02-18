package com.example.moviedb.utils

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

sealed class ErrorEntity {
    object Network : ErrorEntity()
    object NotFound : ErrorEntity()
    object ServiceUnavailable : ErrorEntity()
    object Unknown : ErrorEntity()
}

interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
}

class GeneralErrorHandlerImpl @Inject constructor() : ErrorHandler {

    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException -> ErrorEntity.Network
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound
                    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable
                    else -> ErrorEntity.Unknown
                }
            }
            else -> ErrorEntity.Unknown
        }
    }
}

open class Result<T>(val data: T? = null, val error: ErrorEntity? = null) {
    companion object {
        fun <T> fromData(data: T): Result<T> {
            return Result(data, null)
        }

        fun <T> fromError(error: ErrorEntity): Result<T> {
            return Result(null, error)
        }
    }

    fun isSuccess(): Boolean {
        return data != null
    }
}

fun defineErrorType(error: ErrorEntity): String {
    return  when (error) {
        is ErrorEntity.Network -> "NO INTERNET CONNECTION"
        is ErrorEntity.NotFound -> "CONTENT NOT FOUND"
        is ErrorEntity.ServiceUnavailable -> "CURRENTLY SERVER IS UNAVAILABLE"
        is ErrorEntity.Unknown -> "UNKNOWN ERROR"
    }
}