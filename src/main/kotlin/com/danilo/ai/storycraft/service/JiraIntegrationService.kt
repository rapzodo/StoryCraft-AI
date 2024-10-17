package com.danilo.ai.storycraft.service

import com.danilo.ai.storycraft.model.JiraUserStory
import com.danilo.ai.storycraft.util.callApi
import com.danilo.ai.storycraft.util.formatJiraDescription
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

@Service
class JiraIntegrationService(
    private val okHttpClient: OkHttpClient,
    private val objectMapper: ObjectMapper,
    private val apiProperties: com.danilo.ai.storycraft.config.ApiProperties,
    private val nlpService: NLPService
) {
    private val logger = LoggerFactory.getLogger(JiraIntegrationService::class.java)

    fun extractJiraStories(featureDescriptionRequest: com.danilo.ai.storycraft.model.FeatureDescriptionRequest): List<JiraUserStory> {
        logger.info("Extracting user stories from feature description")
        val extractedStories = nlpService.extractUserStories(featureDescriptionRequest.text)
        return extractedStories.map {
            JiraUserStory(
                title = it.title,
                description = formatJiraDescription(it.technicalDetails, it.acceptanceCriteria),
                project = featureDescriptionRequest.jiraProject,
                epicLink = featureDescriptionRequest.jiraEpicId,
                plannedSprint = featureDescriptionRequest.jiraPlannedSprint
            )
        }
    }

    fun createJiraIssue(jiraUserStory: JiraUserStory): com.danilo.ai.storycraft.model.JiraResponse {
        val jiraProperties = apiProperties.jira
        val jiraIssueText = objectMapper.writeValueAsString(
            com.danilo.ai.storycraft.model.jiraIssueFromUserStory(
                jiraUserStory
            )
        )

        logger.info("Creating Jira issue from Json : $jiraIssueText")

        val requestBody = jiraIssueText.toRequestBody("application/json".toMediaType())

        return okHttpClient.callApi("${jiraProperties.url}/issue", HttpMethod.POST, requestBody, jiraProperties.token)
            .let { objectMapper.readValue(it, com.danilo.ai.storycraft.model.JiraResponse::class.java) }
    }

}

