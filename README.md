# UUID Insert — IntelliJ IDEA Plugin

A lightweight IntelliJ IDEA plugin that inserts a UUID at the current cursor position with a single shortcut.

**Hotkey**: `Alt+Shift+U` | **IDE**: IntelliJ 2025.1+ | **Java**: 21

Works flawlessly in **GigaIDE** (Russian fork of IntelliJ IDEA) as well.

---

## Features

- **Insert UUID at cursor** — press `Alt+Shift+U` to insert a UUID at the current editor position.
- **Multi-caret support** — place multiple carets and insert a unique UUID at each position.
- **Replace selected text** — if text is selected before pressing the shortcut, it is replaced with the generated UUID.
- **Single undoable action** — the insert/replace is wrapped in a `CommandProcessor` command, so one `Ctrl+Z` reverts the entire operation.
- **Flexible format** — configure four settings in `Settings → Tools → UUID Plugin` for any UUID style.
- **Persisted settings** — your preferences survive IDE restarts.

### Format Configuration

Instead of a fixed list of formats, combine four settings to build any UUID style:

| Setting | Description | Limits |
|---|---|---|
| **Case** | Upper or lower case | Checkbox |
| **Delimiter** | Replaces dashes between UUID segments | Up to 2 chars; empty = no dashes |
| **Left Brace** | Character(s) before the UUID | Up to 2 chars; empty = no left wrapping |
| **Right Brace** | Character(s) after the UUID | Up to 2 chars; empty = no right wrapping |

Examples with the same `550e8400-e29b-41d4-a716-446655440000` UUID:

| Case | Delimiter | Left Brace | Right Brace | Result |
|---|---|---|---|---|
| Lower | `-` | _(empty)_ | _(empty)_ | `550e8400-e29b-41d4-a716-446655440000` |
| Upper | `-` | _(empty)_ | _(empty)_ | `550E8400-E29B-41D4-A716-446655440000` |
| Lower | _(empty)_ | _(empty)_ | _(empty)_ | `550e8400e29b41d4a716446655440000` |
| Lower | `-` | `{` | `}` | `{550e8400-e29b-41d4-a716-446655440000}` |
| Lower | `_` | _(empty)_ | _(empty)_ | `550e8400_e29b_41d4_a716_446655440000` |
| Upper | _(empty)_ | `[` | `]` | `[550E8400E29B41D4A716446655440000]` |
| Lower | `-` | `[` | `)` | `[550e8400-e29b-41d4-a716-446655440000)` |

A **live preview** updates in real time as you edit delimiter and braces in the settings panel.

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
2. Check/uncheck **Upper case** for case toggling.
3. Enter up to 2 characters in **Delimiter** (e.g. `-`, `_`, or leave empty for no dashes).
4. Enter up to 2 characters in **Left Brace** (e.g. `{`, `[`, or leave empty for none).
5. Enter up to 2 characters in **Right Brace** (e.g. `}`, `]`, or leave empty for none).
6. See the **live preview** update as you type.
7. Click **Apply**.

The new format takes effect immediately — no IDE restart required.

---

## Installation

### From ZIP archive

1. Build the plugin (see [Development](#development) below) or download the release ZIP.
2. Open IntelliJ IDEA (or **GigaIDE**) → **File → Settings → Plugins → ⚙️ → Install Plugin from Disk…**.
3. Select `uuid-plugin-1.1.2.zip`.
4. Restart the IDE.

### From source

```bash
gradle clean buildPlugin
```

The plugin artifact is created at:
```
build/distributions/uuid-plugin-1.1.2.zip
```

---

## Plugin Architecture

The plugin consists of four Java classes and a `plugin.xml` descriptor:

```
src/
├── main/java/com/th0rn/uuidplugin/
│   ├── UUIDFormat.java              # Utility — generate(uppercase, delimiter, leftBrace, rightBrace)
│   ├── UUIDSettings.java            # PersistentStateComponent — stores 4 user settings
│   ├── UUIDInsertAction.java        # AnAction — inserts/replaces UUID at cursor
│   └── UUIDSettingsConfigurable.java # SearchableConfigurable — settings UI with live preview
├── main/resources/META-INF/
│   └── plugin.xml                   # Action registration, keymap, extensions
└── test/java/com/th0rn/uuidplugin/
    └── UUIDFormatTest.java          # 11 unit tests for all UUID formats
```

### Class responsibilities

| Class | Role | IntelliJ dependencies |
|---|---|---|
| `UUIDFormat` | Pure-Java utility. Generates UUID strings from four parameters: case, delimiter, left brace, right brace. | None |
| `UUIDSettings` | Implements `PersistentStateComponent`. Stores `uppercase`, `delimiter`, `leftBrace`, and `rightBrace`. | `@State`, `@Storage`, `ApplicationManager` |
| `UUIDInsertAction` | Extends `AnAction`. Gets editor, reads settings, generates UUID via `UUIDFormat.generate()`, inserts/replaces text via `WriteCommandAction`. | `AnAction`, `WriteCommandAction`, `Editor` |
| `UUIDSettingsConfigurable` | Implements `SearchableConfigurable`. Renders checkbox + text fields with live preview under Tools settings. | `SearchableConfigurable`, Swing |

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
| `gradle unitTest` | 11 unit tests in `UUIDFormatTest` |
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
| Version | `1.1.2` |
| IntelliJ since-build | `251.*` |
| IntelliJ plugin version | `2.0.0` |
| Java source/target | `21` |
| Persisted config file | `uuid-plugin.xml` (IDE config directory) |
| Plugin ZIP location | `build/distributions/uuid-plugin-1.1.2.zip` |

---

## License

MIT