package com.danilo.ai.storycraft.service

import OpenAIResponse
import com.danilo.ai.storycraft.model.Message
import com.danilo.ai.storycraft.model.OpenAIChatRequest
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class NLPService(
    private val okHttpClient: OkHttpClient,
    private val objectMapper: ObjectMapper,
    @Value("\${api.openai.token}") private val openaiApiKey: String,
    @Value("\${api.openai.url}") private val openaiApiUrl: String,
    @Value("\${api.openai.model}") private val openaiApiModel: String
) {
    val logger: Logger = LoggerFactory.getLogger(NLPService::class.java)

    fun extractUserStories(description: String): List<com.danilo.ai.storycraft.model.GeneratedStoryResponse> {
        logger.info("extracting stories using model $openaiApiModel")
        val messages = listOf(
            Message(
                role = "system",
                content = "You are a Software Engineer that helps to create user and coding stories from descriptions."
            ),
            Message(
                role = "user", content = generatePrompt(description)
            )
        )

        val openAIRequest = OpenAIChatRequest(
            model = openaiApiModel,
            messages = messages
        )

        val requestBody = objectMapper.writeValueAsString(openAIRequest).toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(openaiApiUrl)
            .addHeader("Authorization", "Bearer $openaiApiKey")
            .post(requestBody)
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw RuntimeException("Unexpected code ${response.code}")

            val responseBody = response.body?.string() ?: throw RuntimeException("Response body is null")
            val openAIResponse = objectMapper.readValue(responseBody, OpenAIResponse::class.java)

            logger.info("OpenAI response: $openAIResponse")

            val storiesJson = openAIResponse.choices.firstOrNull()?.message?.content ?: return emptyList()

            return parseUserStories(storiesJson)
        }
    }

    private fun parseUserStories(json: String): List<com.danilo.ai.storycraft.model.GeneratedStoryResponse> {
        return objectMapper.readValue(json, object : TypeReference<List<com.danilo.ai.storycraft.model.GeneratedStoryResponse>>() {})
    }

    private fun generatePrompt(description: String): String {
        return """
    Please analyze the following feature description and break it down into detailed technical Jira stories for the development team. Each story should:

    - Use technical language appropriate for software developers.
    - Include specific implementation details, such as technologies to be used, data models, algorithms, and integration points.
    - Define acceptance criteria that cover functionality, performance, and compliance requirements.

    Present each story as a JSON object with the following format:

    [{
        "title": "Technical Story Title",
        "description": "As a [system/component/user role], I need [technical requirement], so that [goal].",
        "technical_details": "Detailed technical implementation plan.",
        "acceptance_criteria": [
            "Functional acceptance criterion",
            "Compliance acceptance criterion"
        ]
    }]

    Here's the feature description:

    $description
    """.trimIndent()
    }
}

