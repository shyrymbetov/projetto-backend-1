package kz.innlab.fileservice.dto

class ModelSearchResponse {
    var results: List<ModelSearchList>? = null
}

class ModelSearchList {
    var uid: String? = null
    var animationCount: Integer? = null
    var viewerUrl: String? = null
    var publishedAt: String? = null
    var likeCount: Integer? = null
    var isDownloadable: Boolean? = null
    var name: String? = null
    var viewCount: Integer? = null
    var isPublished: Boolean? = null
    var staffpickedAt: String? = null
    var embedUrl: String? = null
}
