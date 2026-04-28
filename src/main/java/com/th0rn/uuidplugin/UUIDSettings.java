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

    private UUIDFormat format = UUIDFormat.STANDARD;

    public UUIDFormat getFormat() {
        return format;
    }

    public void setFormat(UUIDFormat format) {
        this.format = format;
    }

    @Nullable
    @Override
    public UUIDSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull UUIDSettings state) {
        this.format = state.format;
    }

    public static UUIDSettings getInstance() {
        return ApplicationManager.getApplication().getService(UUIDSettings.class);
    }
}
