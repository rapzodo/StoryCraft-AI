package com.danilo.ai.storycraft.model

import com.danilo.ai.storycraft.config.AppConfig
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DescriptionInputTest : StringSpec({

    "should deserialize JSON string to DescriptionInput object" {
        val json = """{"text": "As a user, I want to log in so that I can access my account.", "jiraProject": "PROJ", "jiraBoard": "BOARD", "jiraEpicId": "EPIC-123", "jiraPlannedSprint": "Sprint 1"}"""
        val objectMapper = AppConfig().objectMapper()

        val featureDescriptionRequest: com.danilo.ai.storycraft.model.FeatureDescriptionRequest = objectMapper.readValue(json)

        featureDescriptionRequest.text shouldBe "As a user, I want to log in so that I can access my account."
    }
})
