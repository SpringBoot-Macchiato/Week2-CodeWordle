# CodeWordle

A modern educational word-guessing game focused on programming and technology terminology, inspired by the classic Wordle game.

## Overview

CodeWordle is a web-based application that allows users to play word-guessing games with technical vocabulary from different programming domains including Java, Spring Framework, DevOps, and Databases. The game follows the classic Wordle mechanics with a focus on educational programming terms.

## Key Features

- **Thematic Learning**: Organized word sets across four technology domains (Java, Spring, DevOps, Databases)
- **Flexible Word Validation**: Accepts any valid 5-letter word for gameplay flexibility
- **Real-time Feedback**: Color-coded visual feedback system for letter positions
- **Modern UI**: Glassmorphism design with gradient backgrounds and smooth animations
- **Persistent State**: Game sessions and attempts stored in H2 database
- **Responsive Design**: Fully responsive interface built with Tailwind CSS
- **AJAX Integration**: Seamless gameplay without page reloads

## Technology Stack

**Backend**
- Java 21
- Spring Boot 3.5.7
- Spring JDBC with JdbcTemplate
- H2 Database (in-memory)
- Jakarta Bean Validation

**Frontend**
- JSP with JSTL
- Tailwind CSS 4 (via CDN)
- Vanilla JavaScript (ES6+)
- Fetch API for AJAX requests

**Testing**
- JUnit 5
- Mockito
- Spring Boot Test

**Build Tool**
- Maven 3.6+

## Prerequisites

- Java Development Kit (JDK) 21 or higher
- Apache Maven 3.6 or higher
- Modern web browser with JavaScript enabled

## Installation and Setup

### Clone and Build

```bash
# Clone the repository
git clone <repository-url>
cd codewordle

# Compile the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

### Access the Application

Navigate to `http://localhost:8080` in your web browser.

## Application Architecture

### Project Structure

```
src/main/java/com/crudzaso/codewordle/
├── controller/          # MVC and REST controllers
│   ├── ViewController.java
│   └── GameRestController.java
├── service/             # Business logic layer
│   ├── GameService.java
│   ├── WordValidationService.java
│   ├── GameSessionService.java
│   └── AttemptService.java
├── repository/          # Data access layer
│   ├── ThemeRepository.java
│   ├── WordRepository.java
│   ├── GameSessionRepository.java
│   └── AttemptRepository.java
└── model/               # Domain models
    ├── Theme.java
    ├── Word.java
    ├── GameSession.java
    ├── Attempt.java
    ├── GuessValidation.java
    └── LetterFeedback.java

src/main/resources/
├── application.properties
├── schema.sql           # Database schema
└── data.sql             # Initial data

src/main/webapp/WEB-INF/views/
├── home.jsp             # Theme selection page
└── game.jsp             # Game interface
```

### API Endpoints

**Web MVC Endpoints**
- `GET /` - Home page with theme selection
- `POST /start-game` - Initialize new game session
- `GET /game/{sessionId}` - Game interface

**REST Endpoints**
- `POST /game/{sessionId}/guess` - Submit word guess (returns JSON)

### Response Format

```json
{
  "valid": true,
  "message": "Valid guess",
  "feedback": [
    {"letter": "C", "status": "CORRECT", "position": 0},
    {"letter": "L", "status": "PRESENT", "position": 1},
    {"letter": "A", "status": "ABSENT", "position": 2}
  ],
  "gameWon": false,
  "gameOver": false
}
```

## Game Rules

1. Select a programming theme from the home page
2. Enter any 5-letter word using the input boxes
3. Submit your guess to receive feedback
4. Interpret the color-coded feedback:
   - **Green**: Letter is correct and in the right position
   - **Yellow**: Letter exists in the word but in wrong position
   - **Gray**: Letter does not exist in the target word
5. Win by guessing the correct word within 6 attempts

## Database Configuration

The application uses H2 in-memory database. To access the H2 console during development:

1. Start the application
2. Navigate to `http://localhost:8080/h2-console`
3. Use the following credentials:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

### Database Schema

**Tables**
- `themes` - Programming themes/categories
- `words` - 5-letter words associated with themes
- `game_sessions` - Active and completed game sessions
- `attempts` - Player guesses and feedback history

## Testing

Execute the test suite:

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report
```

The project includes:
- Unit tests for services
- Integration tests for full game flow
- Repository tests for data access

## Development Notes

### Adding New Themes

Edit `src/main/resources/data.sql`:

```sql
INSERT INTO themes (name, description) VALUES
('THEME_NAME', 'Theme description');

INSERT INTO words (word, theme_id) VALUES
('WORD1', <theme_id>),
('WORD2', <theme_id>);
```

### Customizing Styles

The UI uses Tailwind CSS utility classes. Modify the JSP files to adjust styles:
- `home.jsp` - Theme selection and instructions
- `game.jsp` - Game board and input interface

### Word Validation Logic

The application accepts any 5-letter word composed of alphabetic characters (A-Z). The `WordValidationService` handles validation rules:

```java
public boolean isValidGuess(String guess, String targetWord, List<String> validWords) {
    if (guess == null || targetWord == null) return false;
    if (guess.length() != targetWord.length()) return false;
    return guess.matches("^[a-zA-Z]+$");
}
```

## Configuration

Application properties are located in `src/main/resources/application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
```

## License

This project is developed for educational purposes.

## Contributors

Developed as part of a Spring Boot learning project.
