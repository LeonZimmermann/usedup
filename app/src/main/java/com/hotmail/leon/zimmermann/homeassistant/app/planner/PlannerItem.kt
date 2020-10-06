package com.hotmail.leon.zimmermann.homeassistant.app.planner

import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Meal
import java.time.LocalDate

data class PlannerItem(val date: LocalDate, val meal: Meal)