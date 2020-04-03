package com.hotmail.leon.zimmermann.homeassistant.models.tables.category

enum class Category {
    MEAT,
    FISH,
    VEGETABLES,
    FRUITS,
    COSMETICS,
    HYGIENE,
    CLEANING,
    BREAD,
    DRINKS,
    BABY;

    val id: Long = ordinal.toLong()
}