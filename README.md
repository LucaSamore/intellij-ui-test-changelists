# intellij-ui-test-changelists

![UI Tests](https://github.com/LucaSamore/intellij-ui-test-changelists/actions/workflows/test.yaml/badge.svg)

Integration UI test suite for IntelliJ IDEA that automates and verifies the behavior of the
**Version Control → Changelists** settings panel, built with the
[IntelliJ IDE Starter](https://github.com/JetBrains/intellij-ide-starter) framework and the Driver SDK.

## Test Scenarios

The suite covers the following scenarios:

- **Enable "Create changelists automatically"**: opens Settings, navigates to Version Control → Changelists,
  selects the checkbox if not already selected, and verifies it is checked before confirming with OK.

- **Setting is persisted after clicking OK and reopening settings**: enables the checkbox, confirms with OK,
  reopens Settings, and verifies the checkbox is still selected.

- **Setting is not persisted after clicking Cancel**: reads the initial checkbox state, toggles it,
  clicks Cancel instead of OK, reopens Settings, and verifies the state is unchanged.

## Demo

> TODO: add video link here

## Tech Stack

- **Language**: Kotlin
- **Build system**: Gradle (Kotlin DSL)
- **Test framework**: JUnit 5
- **IDE automation**: [IntelliJ IDE Starter](https://github.com/JetBrains/intellij-ide-starter) + Driver SDK
- **IDE under test**: IntelliJ IDEA Community Edition (latest stable release)
- **Test project**: [Quantum-Starter-Kit](https://github.com/Perfecto-Quantum/Quantum-Starter-Kit)

## Prerequisites

- JDK 25
- macOS, Linux, or Windows
- Internet connection (the framework downloads IntelliJ IDEA automatically on first run)

## Running the Tests

```bash
./gradlew test
```

On the first run, the framework will automatically download IntelliJ IDEA Community Edition.
Subsequent runs will reuse the cached installation.

Test reports are generated at:

```
build/reports/tests/test/index.html
```

IDE logs (useful for debugging failures) are available at:

```
out/ide-tests/tests/**/log
```

## CI

The test suite runs automatically on every push and pull request via GitHub Actions.

Each test run:
1. Sets up JDK 25
2. Downloads IntelliJ IDEA Community Edition
3. Executes all tests
4. Uploads test reports and IDE logs as artifacts on failure

## Design Decisions
Using Community Edition removes any licensing dependency, making the project fully self-contained
and runnable by anyone without additional configuration.

## Known Limitations
The checkbox label `"Create changelists automatically"` is locale-dependent. The tests assume the IDE is running in English, which is the default.