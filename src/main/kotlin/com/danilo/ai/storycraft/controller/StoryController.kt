package com.danilo.ai.storycraft.controller

import com.danilo.ai.storycraft.model.JiraUserStory
import com.danilo.ai.storycraft.service.JiraIntegrationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stories")
class StoryController(
    private val jiraIntegrationService: JiraIntegrationService,
) {

    //converts NL feature description into Jira stories
    @PostMapping("/extract")
    @ResponseStatus(HttpStatus.OK)
    fun generateStories(@RequestBody featureDescriptionRequest: com.danilo.ai.storycraft.model.FeatureDescriptionRequest): List<JiraUserStory> {
        return jiraIntegrationService.extractJiraStories(featureDescriptionRequest)
    }

    @PostMapping("/issues")
    @ResponseStatus(HttpStatus.CREATED)
    fun createJiraIssues(@RequestBody jiraUserStories: List<JiraUserStory>): List<com.danilo.ai.storycraft.model.JiraResponse> {
        return jiraUserStories.map {
            jiraIntegrationService.createJiraIssue(it)
        }
    }
}
