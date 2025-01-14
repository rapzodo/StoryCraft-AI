# StoryCraft AI

StoryCraft is an AI-powered application that extracts user stories from text descriptions and creates them as issues in Jira. This application uses Spring Boot, Kotlin, with integrations for OpenAI API and Jira.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Setup](#setup)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Configuration](#configuration)
- [Usage](#usage)
- [Testing](#testing)
- [SSL Certificate Handling](#ssl-certificate-handling)
- [Contributing](#contributing)
- [License](#license)

## Features

- Extract user stories from text descriptions using OpenAI GPT-4.
- Create Jira issues from extracted user stories.
- Spring Boot backend with Kotlin.

## Architecture

The application consists of the following components:

- **NLPService**: Processes text descriptions using OpenAI API.
- **JiraIntegrationService**: Creates issues in Jira based on extracted user stories.
- **UserStoryController**: REST controller for handling requests.

## Setup

### Prerequisites

- JDK 11 or higher
- Gradle
- OpenAI token
- Jira API Token

### Installation

1. **Clone the repository**:

    ```sh
    git clone https://github.com/your-username/storycraft.git
    cd storycraft
    ```

2. **Build the project**:

   Using Maven:
    ```sh
    ./mvnw clean install
    ```

   Using Gradle:
    ```sh
    ./gradlew clean build
    ```

3. **Run the application**:

   Using Maven:
    ```sh
    ./mvnw spring-boot:run
    ```

   Using Gradle:
    ```sh
    ./gradlew bootRun
    ```

### Configuration

1. **Configure OpenAI API Key**:

   Add your OpenAI API token to `src/main/resources/application.yml`:
    ```yaml
    api:
      openai:
        key: your_token
    ```


2**Configure Jira Credentials**:

   Add your Jira token to `src/main/resources/application.yml`:
    ```yaml
    jira:
      url: https://your-jira-instance.atlassian.net
      apiToken: your-api-token
    ```

## Usage

1. **Extract User Stories**:

   Send a POST request to `/api/user-stories/extract` with a JSON body containing the text description:
    ```json
    {
      "text": "As a user, I want to log in so that I can access my account.",
      "jiraProject": "Your Jira Project Key",
	     "jiraBoard":"Your Jira Board Name",
	     "jiraEpicId":"XXXX-XXXX"
    }
    ```

2. **Response**:

   The response will contain the extracted user stories and their Jira issue URLs.
   ```json
   [
      {
      "id": "9999999",
      "key": "JIRA-ISSUE-ID",
      "self": "https://jira.atlassian.net/rest/api/3/issue/9999999"
      }
   ]
   ```
## Testing

1. **Run tests**:

   Using Maven:
    ```sh
    ./mvnw test
    ```

   Using Gradle:
    ```sh
    ./gradlew test
    ```

## SSL Certificate Handling

To handle SSL certificate issues when connecting to Jira, you need to import the Jira server's SSL certificate into the Java trust store:

1. **Extract the Certificate**:
    ```sh
    openssl s_client -showcerts -connect your-jira-instance.atlassian.net:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > jira-cert.crt
    ```

2. **Import the Certificate**:
    ```sh
    sudo keytool -import -alias jira-cert -keystore $JAVA_HOME/lib/security/cacerts -file jira-cert.crt
    ```

    - Default password for the Java trust store is `changeit`.
    - Confirm the import by typing `yes` when prompted.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## License

This project is licensed under my personal license.

NEXT : Integrate with slack (Slackbot ?)
