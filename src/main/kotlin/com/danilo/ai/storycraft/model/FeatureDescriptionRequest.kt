package com.danilo.ai.storycraft.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class FeatureDescriptionRequest @JsonCreator constructor(
    @JsonProperty("text") val text: String,
    @JsonProperty("jiraProject") val jiraProject: String,
    @JsonProperty("jiraBoard") val jiraBoard: String? = null,
    @JsonProperty("jiraEpicId") val jiraEpicId: String? = null,
    @JsonProperty("jiraPlannedSprint") val jiraPlannedSprint: String? = null,
)
