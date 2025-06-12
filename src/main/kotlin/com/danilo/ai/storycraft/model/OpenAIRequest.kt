package com.danilo.ai.storycraft.model

data class OpenAIChatRequest(
    val model: String,
    val messages: List<ChatMessage>
)

data class ChatMessage(
    val role: String,
    val content: String
)

