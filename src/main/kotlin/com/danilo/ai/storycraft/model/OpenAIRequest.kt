package com.danilo.ai.storycraft.model

data class OpenAIChatRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

