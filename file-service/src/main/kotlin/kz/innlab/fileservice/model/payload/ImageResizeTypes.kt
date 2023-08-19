package kz.innlab.fileservice.model.payload

enum class ImageResizeTypes {
    MD {
        override val scaledSize: Int = 500
    },
    SM {
        override val scaledSize: Int = 250
    },
    XS {
        override val scaledSize: Int = 75
    };

    abstract val scaledSize: Int
}
