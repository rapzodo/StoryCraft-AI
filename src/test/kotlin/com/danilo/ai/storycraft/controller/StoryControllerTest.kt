package com.danilo.ai.storycraft.controller

import com.danilo.ai.storycraft.model.FeatureDescriptionRequest
import com.danilo.ai.storycraft.model.JiraResponse
import com.danilo.ai.storycraft.model.JiraUserStory
import com.danilo.ai.storycraft.service.JiraIntegrationService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class StoryControllerTest : FunSpec({

    val jiraService = mockk<JiraIntegrationService>()
    val controller = StoryController(jiraService)

    test("generateStories delegates to JiraIntegrationService") {
        val request = FeatureDescriptionRequest("desc", "PROJ", "BOARD", "EPIC-1", "Sprint 1")
        val stories = listOf(JiraUserStory(title = "t", description = "d", project = "PROJ", epicLink = "EPIC-1", plannedSprint = "Sprint 1"))
        every { jiraService.extractJiraStories(request) } returns stories

        controller.generateStories(request) shouldBe stories

        verify(exactly = 1) { jiraService.extractJiraStories(request) }
    }

    test("createJiraIssues delegates to JiraIntegrationService") {
        val userStory = JiraUserStory(title = "t", description = "d", project = "PROJ")
        val response = JiraResponse("1", "KEY", "self")
        every { jiraService.createJiraIssue(userStory) } returns response

        controller.createJiraIssues(listOf(userStory)) shouldBe listOf(response)

        verify(exactly = 1) { jiraService.createJiraIssue(userStory) }
    }
})
