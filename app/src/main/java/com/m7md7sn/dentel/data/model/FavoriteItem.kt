package com.m7md7sn.dentel.data.model

import android.os.Parcelable
import com.m7md7sn.dentel.data.repository.FavoriteType
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteItem(
    var id: String = "",
    var title: String = "",
    var type: FavoriteType = FavoriteType.ARTICLE
) : Parcelable 