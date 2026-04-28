# UUID Insert — IntelliJ IDEA Plugin

A lightweight IntelliJ IDEA plugin that inserts a UUID at the current cursor position with a single shortcut.

**Hotkey**: `Alt+Shift+U` | **IDE**: IntelliJ 2025.1+ | **Java**: 21

---

## Features

- **Insert UUID at cursor** — press `Alt+Shift+U` to insert a UUID at the current editor position.
- **Multi-caret support** — place multiple carets and insert a unique UUID at each position.
- **Replace selected text** — if text is selected before pressing the shortcut, it is replaced with the generated UUID.
- **Single undoable action** — the insert/replace is wrapped in a `CommandProcessor` command, so one `Ctrl+Z` reverts the entire operation.
- **Configurable format** — choose from five UUID formats in `Settings → Tools → UUID Plugin`.
- **Persisted settings** — your chosen format survives IDE restarts.

### Available UUID Formats

| Format | Display Name | Example |
|---|---|---|
| `STANDARD` | Standard | `550e8400-e29b-41d4-a716-446655440000` |
| `UPPER` | Upper Case | `550E8400-E29B-41D4-A716-446655440000` |
| `NO_DASHES` | No Dashes | `550e8400e29b41d4a716446655440000` |
| `CURLY_BRACES` | Curly Braces | `{550e8400-e29b-41d4-a716-446655440000}` |
| `UNDERSCORE` | Underscore | `550e8400_e29b_41d4_a716_446655440000` |

---

## Usage

1. Open any editor in IntelliJ IDEA.
2. Place the cursor where you want the UUID.
3. Press **`Alt+Shift+U`**.
4. A UUID in your chosen format is inserted.

If text is selected, the UUID replaces the selection. You can also trigger the action from:
- **Editor context menu** (right-click) → *Insert UUID*
- **Tools menu** → *Insert UUID*
- **Generate popup** (`Alt+Insert` or Code → Generate) → *Insert UUID*

Multiple carets are supported — each caret gets its own unique UUID in one operation.

### Changing the format

1. Go to **File → Settings → Tools → UUID Plugin**.
2. Select your preferred format from the dropdown.
3. Click **Apply**.

The new format takes effect immediately — no IDE restart required.

---

## Installation

### From ZIP archive

1. Build the plugin (see [Development](#development) below) or download the release ZIP.
2. Open IntelliJ IDEA → **File → Settings → Plugins → ⚙️ → Install Plugin from Disk…**.
3. Select `uuid-plugin-1.0.7.zip`.
4. Restart the IDE.

### From source

```bash
gradle clean buildPlugin
```

The plugin artifact is created at:
```
build/distributions/uuid-plugin-1.0.7.zip
```

---

## Plugin Architecture

The plugin consists of four Java classes and a `plugin.xml` descriptor:

```
src/
├── main/java/com/th0rn/uuidplugin/
│   ├── UUIDFormat.java              # Enum: five UUID formats with generate()
│   ├── UUIDSettings.java            # PersistentStateComponent — stores chosen format
│   ├── UUIDInsertAction.java        # AnAction — inserts/replaces UUID at cursor
│   └── UUIDSettingsConfigurable.java # SearchableConfigurable — settings UI panel
├── main/resources/META-INF/
│   └── plugin.xml                   # Action registration, keymap, extensions
└── test/java/com/th0rn/uuidplugin/
    └── UUIDFormatTest.java          # 13 unit tests for all UUID formats
```

### Class responsibilities

| Class | Role | IntelliJ dependencies |
|---|---|---|
| `UUIDFormat` | Pure-Java enum. Each variant knows how to `generate()` a UUID string. | None |
| `UUIDSettings` | Implements `PersistentStateComponent`. Stores the user's chosen `UUIDFormat`. | `@State`, `@Storage`, `ApplicationManager` |
| `UUIDInsertAction` | Extends `AnAction`. Gets editor, reads settings, generates UUID, inserts/replaces text via `CommandProcessor`. | `AnAction`, `CommandProcessor`, `Editor` |
| `UUIDSettingsConfigurable` | Implements `SearchableConfigurable`. Renders a combo box for format selection under Tools settings. | `SearchableConfigurable`, Swing |

### Action registration

Defined in `plugin.xml`:
- **Action ID**: `UUIDInsertAction`
- **Groups**: `EditorPopupMenu` (right-click), `ToolsMenu` (Tools dropdown)
- **Shortcut**: `Alt+Shift+U` on the default keymap
- **Platform dependency**: `com.intellij.modules.platform`

---

## Development

### Prerequisites

- **Java 21** (JDK)
- **Gradle 8.5+** — the system `gradle` (4.4.1 on Ubuntu) is too old. Install Gradle 8.5 explicitly.
- **IntelliJ IDEA 2025.1** (Community Edition used as build dependency)

### Setup

```bash
# Point to the correct Gradle version
export PATH="$HOME/.local/tools/gradle-8.5/bin:$PATH"

# Clone and enter the project
git clone git@github.com:feag0r/uuid-plugin.git
cd uuid-plugin
```

### Build commands

```bash
# Full build: clean, run unit tests, package the plugin ZIP
gradle clean unitTest buildPlugin

# Compile only (no tests)
gradle compileJava

# Run unit tests only
gradle unitTest

# Package the plugin without running tests
gradle buildPlugin
```

### Testing

Tests use **JUnit 5** (Jupiter).

| Command | What it runs |
|---|---|
| `gradle unitTest` | 13 unit tests in `UUIDFormatTest` |
| `gradle test` | **Disabled** — see below |

#### Why `gradle test` is disabled

The IntelliJ Platform Gradle Plugin intercepts the `test` task and runs it inside an IntelliJ IDE sandbox. This sandboxed runner fails with a cryptic `Index: 1, Size: 1` error for JUnit 5 tests. Instead, the `unitTest` task runs tests using the standard Gradle `Test` infrastructure outside the sandbox.

**Important**: only tests that do **not** import `com.intellij.*` classes can use `unitTest`. Tests requiring IntelliJ Platform APIs (e.g., `UUISettings`) need the sandboxed `test` task, which is currently broken.

The `check` lifecycle task depends on `unitTest`, so `gradle build` still runs tests correctly.

---

## Configuration details

| Setting | Value |
|---|---|
| Plugin ID | `com.th0rn.uuid-plugin` |
| Group ID | `com.th0rn` |
| Artifact ID | `uuid-plugin` |
| Version | `1.0.7` |
| IntelliJ since-build | `251.*` |
| IntelliJ plugin version | `2.0.0` |
| Java source/target | `21` |
| Persisted config file | `uuid-plugin.xml` (IDE config directory) |
| Plugin ZIP location | `build/distributions/uuid-plugin-1.0.7.zip` |

---

## License

MIT