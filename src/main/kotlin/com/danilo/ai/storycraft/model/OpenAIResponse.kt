package com.danilo.ai.storycraft.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class OpenAIResponse @JsonCreator constructor(
    @JsonProperty("id") val id: String,
    @JsonProperty("object") val objectType: String,
    @JsonProperty("created") val created: Long,
    @JsonProperty("model") val model: String,
    @JsonProperty("usage") val usage: Usage,
    @JsonProperty("choices") val choices: List<Choice>
)

data class Usage @JsonCreator constructor(
    @JsonProperty("prompt_tokens") val promptTokens: Int,
    @JsonProperty("completion_tokens") val completionTokens: Int,
    @JsonProperty("total_tokens") val totalTokens: Int
)

data class Choice @JsonCreator constructor(
    @JsonProperty("message") val message: Message,
    @JsonProperty("finish_reason") val finishReason: String,
    @JsonProperty("index") val index: Int
)

data class Message @JsonCreator constructor(
    @JsonProperty("role") val role: String,
    @JsonProperty("content") val content: String
)
