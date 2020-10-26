package com.example.yaplayground

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.iconics.IconicsDrawable

class Icons(private val appContext: Context) {

    companion object {
        @JvmField val COLOR_BLACK = Color.parseColor("#000000")

        private const val REGULAR_ICON_SIZE_DP = 48
    }

    @get:JvmName("shareIcon") val shareIcon: Drawable by lazy { IconicsDrawable(appContext).icon(
        CommunityMaterial.Icon2.cmd_share).color(COLOR_BLACK).sizeDp(REGULAR_ICON_SIZE_DP) }
}