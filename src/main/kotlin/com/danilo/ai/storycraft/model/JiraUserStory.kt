package com.danilo.ai.storycraft.model

data class JiraUserStory(
    val id: String? = null,
    val title: String,
    val description: String,
    var project: String,
    val epicLink: String? = null,
    val plannedSprint: String? = null,
)
