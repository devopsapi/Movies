package com.example.moviedb.data.api.responses


class MovieDetailsDTO(
    val title: String,
    val overview: String,
    val poster_path: String?,
    val release_date: String,
    val vote_average: Double?,
    val genres: List<Genres>,
)

class Genres(val id: Int, val name: String)


class MovieDetails(
    val title: String,
    val overview: String,
    val poster_path: String,
    val releaseDate: String,
    val voteAverage: String,
    val genres: List<Genres>,
)

fun MovieDetailsDTO.convertToMovieDetails(): MovieDetails {

    val title = if (this.title.isNotEmpty()) this.title else "No title"
    val overview = if (this.overview.isNotEmpty()) this.overview else "No overview"
    val poster = if (this.poster_path.isNullOrEmpty()) "" else poster_path
    val releaseDate =
        if (this.release_date.isNotEmpty()) "Release: ${this.release_date}" else "No date"
    val voteAverage = if (this.vote_average != null) "Rating: ${this.vote_average}" else "No votes"
    val genres = if (this.genres.isNullOrEmpty()) listOf(Genres(0, "No genre(s)")) else this.genres

    return MovieDetails(
        title,
        overview,
        poster,
        releaseDate,
        voteAverage,
        genres
    )
}