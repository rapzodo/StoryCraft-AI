package com.danilo.ai.storycraft.model

import com.fasterxml.jackson.annotation.JsonProperty

data class JiraIssueRequest(
    val fields: com.danilo.ai.storycraft.model.Fields
)

fun jiraIssueFromUserStory(jiraUserStory: com.danilo.ai.storycraft.model.JiraUserStory): com.danilo.ai.storycraft.model.JiraIssueRequest {
    return com.danilo.ai.storycraft.model.JiraIssueRequest(
        com.danilo.ai.storycraft.model.Fields(
            project = com.danilo.ai.storycraft.model.Project(jiraUserStory.project),
            summary = jiraUserStory.title,
            description = jiraUserStory.description,
            issuetype = com.danilo.ai.storycraft.model.IssueType("Story"),
            plannedSprint = jiraUserStory.plannedSprint,
            epicLink = jiraUserStory.epicLink
        )
    )
}

data class Fields(
    val project: com.danilo.ai.storycraft.model.Project,
    val summary: String,
    val description: String,
    val issuetype: com.danilo.ai.storycraft.model.IssueType,
    @JsonProperty("customfield_10102")
    val plannedSprint: String?,
    @JsonProperty("customfield_10006")
    val epicLink: String?,
)

data class Project(
    val key: String
)

data class IssueType(
    val name: String
)
