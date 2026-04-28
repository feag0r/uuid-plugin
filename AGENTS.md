# AGENTS.md — UUID Insert IntelliJ Plugin

## Build & Test Commands

```bash
# Full build including tests (use this for CI / end-to-end)
gradle clean unitTest buildPlugin     # creates build/distributions/uuid-plugin-1.0.0.zip

# Just unit tests
gradle unitTest

# Just package (no tests)
gradle buildPlugin
```

**Gradle note**: the system `gradle` is 4.4.1 — too old for the IntelliJ Platform plugin (v2.x). Use Gradle 8.5 installed at:

```bash
export PATH="$HOME/.local/tools/gradle-8.5/bin:$PATH"
```

## Test Quirks

- **`gradle test` is disabled** (`enabled = false`). The IntelliJ Platform Gradle Plugin intercepts the `test` task and runs tests inside an IntelliJ sandbox, which fails with a cryptic `Index: 1, Size: 1` error for JUnit 5. Use `gradle unitTest` instead.
- **`gradle check` → depends on `unitTest`**, so `gradle build` still runs tests correctly via the `check` lifecycle.
- **UUISettingsTest was removed** because UUISettings imports `PersistentStateComponent` and other IntelliJ Platform classes that aren't on the `unitTest` classpath. Any test importing `com.intellij.*` classes must use the IntelliJ sandboxed test task (which is currently broken). Pure-Java tests (like `UUIDFormatTest`) work fine.
- `instrumentTestCode` runs automatically and modifies bytecode in the test output dir. The `unitTest` task reads from `sourceSets.test.java.classesDirectory` (pre-instrumentation) to avoid loading issues.

## Project Architecture

| File | Role |
|---|---|
| `UUIDFormat.java` | Enum with `generate()` — produces UUID string per format. Pure Java, no IntelliJ deps. |
| `UUIDSettings.java` | `PersistentStateComponent` — stores user's chosen format. Persisted to `uuid-plugin.xml` in IDE config. |
| `UUIDInsertAction.java` | `AnAction` — reads format from settings, generates UUID, inserts/replaces at cursor via `CommandProcessor`. |
| `UUISettingsConfigurable.java` | `SearchableConfigurable` — settings UI under Tools → UUID Plugin. |
| `plugin.xml` | Plugin descriptor. Registers action (`Ctrl+Shift+U`), settings service, configurable. |

## Key Details

- **Plugin ID**: `com.th0rn.uuid-plugin`
- **Target IDE**: IntelliJ 2025.1 (since-build `251.*`)
- **Java**: 21
- **Shortcut**: `Ctrl+Shift+U`
- **Action groups**: Added to `EditorPopupMenu` and `ToolsMenu`
- **UUID formats**: STANDARD, UPPER, NO_DASHES, CURLY_BRACES, UNDERSCORE
- **Editor behavior**: replaces selected text if any, otherwise inserts at cursor. Single undoable operation.
- **Artifact location**: `build/distributions/uuid-plugin-1.0.0.zip`
- **`buildSearchableOptions`** is disabled (skipped).
