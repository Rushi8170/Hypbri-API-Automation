# Enterprise API Automation Framework

API test automation framework built with **Java, REST Assured, TestNG, Cucumber (BDD), Maven, Extent Reports, and Jenkins/GitHub Actions CI/CD**.

Structured the way real organizations build API automation: a single reusable REST client, environment-based config, data-driven testing, and both plain TestNG and BDD (Gherkin) styles sharing the same underlying logic.

---

## 1. Tech Stack

| Layer              | Tool/Library                          |
|--------------------|----------------------------------------|
| Language           | Java 11                                |
| API Automation     | REST Assured 5.4                       |
| Test Runner        | TestNG 7.9                             |
| BDD                | Cucumber 7.15 (Gherkin)                |
| Build Tool         | Maven                                  |
| Reporting          | ExtentReports 5.1 (Spark, dark theme)  |
| Logging            | Log4j2                                 |
| Data-Driven        | Apache POI (Excel), Jackson (JSON)     |
| CI/CD              | Jenkins (Jenkinsfile) + GitHub Actions |
| Test Data          | JavaFaker                              |

---

## 2. Folder Structure

```
api-automation-framework/
├── pom.xml                                # All dependencies + Maven profiles
├── Jenkinsfile                            # Jenkins declarative pipeline
├── .github/workflows/ci.yml               # GitHub Actions pipeline
├── .gitignore
├── src/
│   ├── main/java/com/framework/
│   │   ├── config/ConfigManager.java      # Env-based property loader (singleton)
│   │   ├── constants/FrameworkConstants.java
│   │   ├── listeners/
│   │   │   ├── ExtentReportManager.java   # ThreadLocal<ExtentTest>
│   │   │   └── TestListener.java         # ITestListener -> Extent Reports
│   │   ├── api/
│   │   │   ├── client/RestClient.java     # Thin wrapper over REST Assured
│   │   │   ├── endpoints/APIEndpoints.java# All API paths as constants
│   │   │   ├── models/                    # Request/response POJOs
│   │   │   └── validators/ResponseValidator.java
│   │   └── utils/                         # Excel, Json, TestData, ExtentLogger
│   └── test/
│       ├── java/com/framework/
│       │   ├── tests/api/                 # BaseAPITest + TestNG API test classes
│       │   ├── stepdefs/APIStepDefs.java  # Cucumber step definitions
│       │   └── runners/                   # Cucumber-TestNG runners
│       └── resources/
│           ├── features/api/              # .feature files (Gherkin)
│           ├── testdata/TestData.xlsx     # Excel-driven test data
│           ├── testng-suites/             # All testng-*.xml suites
│           └── cucumber.properties
├── reports/                                # ExtentReport.html, cucumber reports
└── logs/                                   # log4j2 rolling logs
```

---

## 3. Architecture Decisions (why it's built this way)

- **One `RestClient`, two consumers** — both plain TestNG API tests and Cucumber step defs call the same `RestClient`. No duplicated HTTP logic.
- **ConfigManager resolves values in priority order**: `-D` system property → CI environment variable → `config-{env}.properties`. Credentials never get committed to Git — CI injects them as secrets at runtime.
- **Maven profiles, not hardcoded suite files** — `mvn test -Pregression`, `-Pparallel`, `-Psmoke`, `-Papi` each map to a different `testng-*.xml`. CI pipelines just switch the `-P` flag.
- **No browser dependency** — this framework is pure HTTP-layer automation, so tests run fast and don't need a browser, driver, or GUI environment. Ideal for microservice/API-first testing and lightweight CI runners.

---

## 4. Prerequisites

- Java 11+ (`java -version`)
- Maven 3.6+ (`mvn -version`)

No browser or driver setup needed — this is a pure API framework.

---

## 5. Setup

```bash
git clone <your-repo-url>
cd api-automation-framework
mvn clean install -DskipTests
```

---

## 6. Running Tests

All execution is driven by Maven profiles defined in `pom.xml`.

| Command                                              | What it runs                                      |
|-------------------------------------------------------|----------------------------------------------------|
| `mvn clean test -Pregression`                          | Full API regression (`testng-regression.xml`)      |
| `mvn clean test -Psmoke`                                | Smoke subset only                                   |
| `mvn clean test -Pparallel`                             | API tests in parallel                               |
| `mvn clean test -Papi`                                  | Same as regression, kept for CI naming consistency  |
| `mvn clean test -Pregression -Denv=dev`                 | Override environment via `-D` flag                  |

### Run Cucumber (BDD) instead of plain TestNG
```bash
mvn clean test -DsuiteFile=src/test/resources/testng-suites/testng-cucumber.xml
```

### Run by TestNG group directly
```bash
mvn clean test -Dgroups=smoke -Psmoke
```

After any run, open:
- **`reports/ExtentReport.html`** — main dashboard (pass/fail, request/response logs)
- **`reports/cucumber-api-report.html`** — if Cucumber runner used
- **`logs/automation.log`** — full execution log

---

## 7. Data-Driven Testing

Excel-based (`src/test/resources/testdata/TestData.xlsx`, sheet `Users`):

```java
@DataProvider(name = "userData")
public Object[][] userDataProvider() {
    return ExcelUtil.getSheetDataAsArray(FrameworkConstants.TESTDATA_EXCEL, "Users");
}

@Test(dataProvider = "userData")
public void testCreateUser(Map<String, String> data) { ... }
```

Add new rows to the Excel sheet — no code change needed.

---

## 8. Adding a New API Endpoint

1. Add the path constant to `APIEndpoints.java`.
2. Add a request/response POJO under `api/models/` if needed.
3. Add reusable assertions to `ResponseValidator.java` if the check is generic (status code, field value, response time).
4. Write the test in `tests/api/` (plain TestNG) or add a scenario + step to `features/api/` + `APIStepDefs.java` (BDD) — both call the same `RestClient`.

---

## 9. CI/CD

### Jenkins
`Jenkinsfile` is a declarative pipeline with parameters (`TEST_SUITE`, `ENVIRONMENT`). It:
1. Checks out code, compiles.
2. Runs `mvn clean test -P<suite>` with the chosen environment.
3. Publishes the Extent HTML report and Cucumber HTML report via `publishHTML`.
4. Publishes TestNG/Surefire XML via the `junit` step.
5. Archives `reports/` and `logs/` as build artifacts.

### GitHub Actions
`.github/workflows/ci.yml` runs on push/PR to `main`/`develop`, plus manual `workflow_dispatch` with a suite picker. It executes the chosen Maven profile and uploads the Extent report, Surefire results, and logs as workflow artifacts.

---

## 10. Git Workflow (suggested)

```bash
git init
git add .
git commit -m "Initial commit: API automation framework"
git branch -M main
git remote add origin <your-github-repo-url>
git push -u origin main
```

Suggested branch strategy: `main` (stable) → `develop` (integration) → `feature/*` branches, with PRs into `develop` triggering the GitHub Actions smoke suite, and merges to `main` triggering full regression.

---

## 11. Troubleshooting

- **`Cannot load config file`** — make sure you're running Maven from the project root, not a subfolder.
- **Connection refused / timeout** — confirm `api.base.url` in `config-qa.properties` / `config-dev.properties` points to a reachable endpoint.
- **Parallel tests stepping on each other** — confirm test classes don't share static mutable state.

---

## 12. Sample API Under Test

This framework is wired to a free public sandbox so it runs out of the box with zero setup:
- API: [https://reqres.in/api](https://reqres.in/api)

Swap `api.base.url` in `config-qa.properties` / `config-dev.properties` to point at your real API — nothing else in the framework needs to change.
