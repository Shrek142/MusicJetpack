package com.example.musicappmvvmjetpack.Model

import com.example.musicappmvvmjetpack.R

data class Music(
    val id: Int,
    val title: String,
    val singer: String,
    val download: Int,
    val song: Int,
    val posterUrl: String,
){
    companion object{
        fun getMusic() = listOf(
            Music(
                id = 1,
                title = "Mu1",
                singer = "Old Trafford",
                download = 100,
                song = R.raw.song2,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
            ),
            Music(
                id = 2,
                title = "Barcelona2",
                singer = "Spotify",
                download = 35,
                song = R.raw.song1,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg"
            ),
            Music(
                id = 3,
                title = "Liverpool3",
                singer = "Anfield",
                download = 23,
                song = R.raw.song3,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg"
            ),
            Music(
                id = 4,
                title = "Emirates4",
                singer = "Arsenal",
                download = 22,
                song = R.raw.song4,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
            ),
            Music(
                id = 5,
                title = "Mc5",
                singer = "Etihad",
                download = 21,
                song = R.raw.song1,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg"
            ),
            Music(
                id = 6,
                title = "Chelsea6",
                singer = "Stamford Bridge",
                download = 20,
                song = R.raw.song3,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg"
            ),
            Music(
                id = 7,
                title = "Mu7",
                singer = "Old Trafford",
                download = 100,
                song = R.raw.song2,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
            ),
            Music(
                id = 8,
                title = "Barcelona8",
                singer = "Spotify",
                download = 35,
                song = R.raw.song1,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg"
            ),
            Music(
                id = 9,
                title = "Liverpool9",
                singer = "Anfield",
                download = 23,
                song = R.raw.song3,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg"
            ),
            Music(
                id = 10,
                title = "Emirates10",
                singer = "Arsenal",
                download = 22,
                song = R.raw.song4,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BNGNhMDIzZTUtNTBlZi00MTRlLWFjM2ItYzViMjE3YzI5MjljXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg"
            ),
            Music(
                id = 11,
                title = "Mc11",
                singer = "Etihad",
                download = 21,
                song = R.raw.song1,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@._V1_.jpg"
            ),
            Music(
                id = 12,
                title = "Chelsea12",
                singer = "Stamford Bridge",
                download = 20,
                song = R.raw.song3,
                posterUrl = "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg"
            ),
        )
    }
}
