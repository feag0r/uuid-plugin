package com.th0rn.uuidplugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
    name = "UUIDPluginSettings",
    storages = @Storage("uuid-plugin.xml")
)
public class UUIDSettings implements PersistentStateComponent<UUIDSettings> {

    private boolean uppercase;
    private String delimiter = "-";
    private String braces = "";

    public boolean isUppercase() {
        return uppercase;
    }

    public void setUppercase(boolean uppercase) {
        this.uppercase = uppercase;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getBraces() {
        return braces;
    }

    public void setBraces(String braces) {
        this.braces = braces;
    }

    @Nullable
    @Override
    public UUIDSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull UUIDSettings state) {
        this.uppercase = state.uppercase;
        this.delimiter = state.delimiter;
        this.braces = state.braces;
    }

    public static UUIDSettings getInstance() {
        return ApplicationManager.getApplication().getService(UUIDSettings.class);
    }
}
