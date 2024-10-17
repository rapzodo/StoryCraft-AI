package com.danilo.ai.storycraft.model

import com.fasterxml.jackson.annotation.JsonProperty

data class GeneratedStoryResponse(
    val title: String,
    val description: String,
    @JsonProperty("technical_details")
    val technicalDetails: String,
    @JsonProperty("acceptance_criteria")
    val acceptanceCriteria: List<String>
)
