import com.danilo.ai.storycraft.config.AppConfig
import com.danilo.ai.storycraft.service.JiraIntegrationService
import com.danilo.ai.storycraft.service.NLPService
import com.danilo.ai.storycraft.model.FeatureDescriptionRequest
import com.danilo.ai.storycraft.model.GeneratedStoryResponse
import com.danilo.ai.storycraft.model.JiraUserStory
import com.danilo.ai.storycraft.util.formatJiraDescription
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class JiraIntegrationServiceTest : FunSpec({

    val okHttpClient = mockk<OkHttpClient>()
    val objectMapper = AppConfig().objectMapper()
    val apiProperties = mockk<com.danilo.ai.storycraft.config.ApiProperties>()
    val nlpService = mockk<NLPService>()
    val jiraIntegrationService = JiraIntegrationService(okHttpClient, objectMapper, apiProperties, nlpService)

    val jiraURL = "https://jira_url"

    beforeTest {
        every { apiProperties.jira } returns com.danilo.ai.storycraft.config.ApiProperties.JiraProperties().apply {
            url = jiraURL
            token = "token"
        }
    }
    afterTest {
        clearAllMocks()
    }

    test("createJiraIssue should return JiraResponse") {
        val jiraUserStory = JiraUserStory("title", "description", "project", "epicLink", "plannedSprint")
        val expectedJiraResponseBody = """{"id":"123","key":"JIRA-123","self":"selfurl"}"""
        val response = Response.Builder()
            .request(Request.Builder().url(jiraURL).build())
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(expectedJiraResponseBody.toResponseBody("application/json".toMediaType()))
            .build()

        every { okHttpClient.newCall(any()).execute() } returns response

        val jiraIssue = jiraIntegrationService.createJiraIssue(jiraUserStory)

        verify(exactly = 1) {
            okHttpClient.newCall(any()).execute()
        }
        jiraIssue shouldBe objectMapper.readValue(expectedJiraResponseBody, com.danilo.ai.storycraft.model.JiraResponse::class.java)
    }

    test("createJiraIssue should throw RuntimeException when response is not successful") {
        val jiraUserStory = JiraUserStory("title", "description", "project", "epicLink", "plannedSprint")
        val expectedJiraResponseBody = """{"errorMessages":["Error"],"errors":{}}"""
        val response = Response.Builder()
            .request(Request.Builder().url(jiraURL).build())
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(400)
            .body(expectedJiraResponseBody.toResponseBody("application/json".toMediaType()))
            .message("Bad Request")
            .build()

        every { okHttpClient.newCall(any()).execute() } returns response

        val exception = shouldThrow<RuntimeException> {
            jiraIntegrationService.createJiraIssue(jiraUserStory)
        }

        verify(exactly = 1) {
            okHttpClient.newCall(any()).execute()
        }

        exception.message shouldBe "Unexpected code 400 : $expectedJiraResponseBody"
    }

    test("extractJiraStories should map NLP stories to JiraUserStory") {
        val featureRequest = FeatureDescriptionRequest("Feature", "PROJ", "BOARD", "EPIC-1", "Sprint 1")
        val generatedStories = listOf(
            GeneratedStoryResponse("title", "desc", "tech", listOf("AC1", "AC2"))
        )

        every { nlpService.extractUserStories("Feature") } returns generatedStories

        val result = jiraIntegrationService.extractJiraStories(featureRequest)

        result shouldBe listOf(
            JiraUserStory(
                title = "title",
                description = formatJiraDescription("tech", listOf("AC1", "AC2")),
                project = "PROJ",
                epicLink = "EPIC-1",
                plannedSprint = "Sprint 1"
            )
        )

        verify(exactly = 1) { nlpService.extractUserStories("Feature") }
    }
})