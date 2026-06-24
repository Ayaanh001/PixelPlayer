package com.theveloper.pixelplay.data.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MusicFolder(
    val path: String,
    val name: String,
    val songs: ImmutableList<Song> = persistentListOf(),
    val subFolders: ImmutableList<MusicFolder> = persistentListOf()
) {
    val totalSongCount: Int by lazy {
        songs.size + subFolders.sumOf { it.totalSongCount }
    }

    val totalSubFolderCount: Int by lazy {
        subFolders.size + subFolders.sumOf { it.totalSubFolderCount }
    }

    fun collectAllSongs(): List<Song> {
        return songs + subFolders.flatMap { it.collectAllSongs() }
    }
}

fun Iterable<MusicFolder>.flattenFolders(includeEmpty: Boolean = false): List<MusicFolder> {
    return flatMap { folder ->
        val current = if (includeEmpty || folder.songs.isNotEmpty()) listOf(folder) else emptyList()
        current + folder.subFolders.flattenFolders(includeEmpty)
    }
}
