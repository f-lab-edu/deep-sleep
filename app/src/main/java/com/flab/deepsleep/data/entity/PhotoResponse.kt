package com.flab.deepsleep.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class PhotoResponse(
    val id: String,
    val created_at: String,
    val updated_at: String,
    val width: Int,
    val height: Int,
    val color: String,
    val blur_hash: String,
    val downloads: Int,
    val likes: Int,
    val liked_by_user: Boolean,
    val description: String?,
    val exif: Exif,
    val location: Location,
    val current_user_collections: List<Collection>,
    val urls: Urls,
    val links: PhotoLinks,
    val user: User
)

@Serializable
data class Exif(
    val make: String?,
    val model: String?,
    val exposure_time: String?,
    val aperture: String?,
    val focal_length: String?,
    val iso: Int?
)

@Serializable
data class Location(
    val name: String?,
    val city: String?,
    val country: String?,
    val position: Position?
)

@Serializable
data class Position(
    val latitude: Double?,
    val longitude: Double?
)

@Serializable
data class Collection(
    val id: Int,
    val title: String,
    val published_at: String,
    val last_collected_at: String,
    val updated_at: String,
    val cover_photo: String?, // Null-safe 타입
    val user: String? // Null-safe 타입
)

@Serializable
data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String,
    val thumb: String
)

@Serializable
data class PhotoLinks(
    val self: String,
    val html: String,
    val download: String,
    val download_location: String
)

@Serializable
data class User(
    val id: String,
    val updated_at: String,
    val username: String,
    val name: String,
    val portfolio_url: String?,
    val bio: String?,
    val location: String?,
    val total_likes: Int,
    val total_photos: Int,
    val total_collections: Int,
    val instagram_username: String?,
    val twitter_username: String?,
    val links: UserLinks
)

@Serializable
data class UserLinks(
    val self: String,
    val html: String,
    val photos: String,
    val likes: String,
    val portfolio: String
)