package com.danilo.ai.storycraft

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import com.danilo.ai.storycraft.config.ApiProperties

@SpringBootApplication
@EnableConfigurationProperties(com.danilo.ai.storycraft.config.ApiProperties::class)
class StoryCraftApplication

fun main(args: Array<String>) {
    runApplication<com.danilo.ai.storycraft.StoryCraftApplication>(*args)
}
