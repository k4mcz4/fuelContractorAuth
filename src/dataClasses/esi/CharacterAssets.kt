package com.fuelContractorAuth.dataClasses.esi

import com.google.gson.annotations.SerializedName

data class CharacterAssetsFromJson(
    @SerializedName("is_blueprint_copy") val isBlueprintCopy: String?,
    @SerializedName("is_singleton") val isSingleton: String?,
    @SerializedName("item_id") val itemId: Long?,
    @SerializedName("location_flag") val locationFlag: String?,
    @SerializedName("location_id") val locationId: Long?,
    @SerializedName("location_type") val locationType: String?,
    @SerializedName("quantity") val quantity: Int?,
    @SerializedName("type_id") val typeId: Long?
)

data class CharacterAssetsCard(
    val itemName: String,
    val itemLocation: String,
    val itemQuantity: Int
)
