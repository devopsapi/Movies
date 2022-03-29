package com.example.moviedb.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviedb.data.api.responses.MoviesResponse
import com.example.moviedb.data.model.Movie

typealias MoviesPageLoader = suspend (pageIndex: Int) -> MoviesResponse

class MoviesPagingSource(
    private val loader: MoviesPageLoader,
    private val pageSize: Int = 20,
) :
    PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1

        return try {
            val response = loader.invoke(page)
            val movies = response.movies
            val nextKey = if (movies.isEmpty()) null else page + (params.loadSize / pageSize)

            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}