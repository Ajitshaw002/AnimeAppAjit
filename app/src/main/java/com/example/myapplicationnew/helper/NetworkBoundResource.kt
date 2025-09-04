package com.example.myapplicationnew.helper

import kotlinx.coroutines.flow.*


inline fun <ResultType, RequestType> networkBoundResource(
    networkMonitor: NetworkMonitor,
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType?) -> Boolean = { true }
): Flow<Resource<ResultType>> = flow {
    emit(Resource.Loading)

    val cached = query().firstOrNull()

    if (cached == null || cachedIsEmpty(cached) || networkMonitor.isConnected()) {
        try {
            val fetched = fetch()
            saveFetchResult(fetched)
            emitAll(query().map { Resource.Success(it) })
        } catch (t: Throwable) {
            if (cached != null && !cachedIsEmpty(cached)) {
                emit(Resource.Success(cached))
            } else {
                emit(Resource.Error(t.message ?: "Something went wrong"))
            }
        }
    } else {
        emitAll(query().map { Resource.Success(it) })
    }
}

 fun <T> cachedIsEmpty(cached: T): Boolean {
    return when (cached) {
        is Collection<*> -> cached.isEmpty()
        else -> false
    }
}


