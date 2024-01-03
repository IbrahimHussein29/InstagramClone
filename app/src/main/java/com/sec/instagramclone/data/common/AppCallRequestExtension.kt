package com.sec.instagramclone.data.common

suspend fun <T : Any> Resource<T>.onSuccess(
    executable: suspend (T) -> Unit
): Resource<T> = apply {
    if (this is Resource.Success<T> && data != null) {
        executable(data)
    }
}

suspend fun <T : Any> Resource<T>.onNullableSuccess(
    executable: suspend (T?) -> Unit
): Resource<T> = apply {
    if (this is Resource.Success<T>) {
        executable(data)
    }
}


suspend fun <T : Any> Resource<T>.onLoading(
    executable: suspend (T?) -> Unit
): Resource<T> = apply {
    if (this is Resource.Loading<T>) {
        executable(data)
    }
}

suspend fun <T : Any> Resource<T>.onError(
    executable: suspend (message: String?, result: T?) -> Unit
): Resource<T> = apply {
    if (this is Resource.Error<T>) {
        executable(message, data)
    }
}
