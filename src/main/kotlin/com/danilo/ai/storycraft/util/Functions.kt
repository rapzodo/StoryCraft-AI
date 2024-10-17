package com.danilo.ai.storycraft.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.springframework.http.HttpMethod


fun OkHttpClient.callApi(
    url: String,
    httpMethod: HttpMethod,
    requestBody: RequestBody? = null,
    token: String
): String {
    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", token)
        .method(httpMethod.name(), requestBody)
        .build()
    newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw RuntimeException("Unexpected code ${response.code} : ${response.body?.string()}")
        return response.body?.string() ?: throw RuntimeException("Response body is null")
    }
}

fun formatJiraDescription(technicalDetails: String, acceptanceCriteria: List<String>): String {
    // Format the acceptance criteria as a numbered list
    val formattedAcceptanceCriteria = acceptanceCriteria
        .mapIndexed { index, criterion -> "${index + 1}. $criterion" }
        .joinToString(separator = "\n")

    // Construct the description using Jira's Wiki Markup
    val description = """
        h2. Technical Details

        $technicalDetails

        h2. Acceptance Criteria

        $formattedAcceptanceCriteria
    """.trimIndent()

    return description
}

fun generatePrompt(description: String): String {
    return """
        Please analyze the following feature description and break it down into detailed technical Jira stories for the development team. Each story should:
        
        - Use technical language appropriate for software developers.
        - Include specific implementation details, such as technologies to be used, data models, algorithms, and integration points.
        - Define acceptance criteria that cover functionality, performance, and compliance requirements.
        
        Present a JSONARRAY and each story as a JSON object with the following format AND DONT ADD ANY TEXT BEFORE THE JSON OBJECT:
        
        {
            "title": "Technical Story Title",
            "description": "As a [system/component/user role], I need [technical requirement], so that [goal].",
            "technical_details": "Detailed technical implementation plan.",
            "acceptance_criteria": [
                "Functional acceptance criterion",
                "Compliance acceptance criterion"
            ]
        }
        
        Here's the feature description:
        
        $description
        """.trimIndent()
}