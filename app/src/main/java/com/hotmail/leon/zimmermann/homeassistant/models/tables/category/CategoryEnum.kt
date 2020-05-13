package com.hotmail.leon.zimmermann.homeassistant.models.tables.category

enum class CategoryEnum(val text: String) {
    MEAT("Meat"),
    FISH("Fish"),
    VEGETABLES("Vegetables"),
    FRUITS("Fruits"),
    COSMETICS("Cosmetics"),
    HYGIENE("Hygiene"),
    CLEANING("Cleaning"),
    BREAD("Bread"),
    DRINKS("Drinks"),
    BABY("Baby");

    val id: Long = ordinal.toLong()
}