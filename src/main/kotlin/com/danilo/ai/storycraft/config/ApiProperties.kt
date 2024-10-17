package com.danilo.ai.storycraft.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "api")
class ApiProperties {
    lateinit var jira: com.danilo.ai.storycraft.config.ApiProperties.JiraProperties
    lateinit var openai: com.danilo.ai.storycraft.config.ApiProperties.OpenAIProperties

    class JiraProperties {
        lateinit var token: String
        lateinit var url: String
    }

    class OpenAIProperties {
        lateinit var token: String
        lateinit var url: String
        lateinit var model: String
    }
}