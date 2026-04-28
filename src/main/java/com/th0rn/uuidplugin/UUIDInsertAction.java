package com.th0rn.uuidplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

public class UUIDInsertAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(UUIDInsertAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        var editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            LOG.warn("No editor available in action context");
            return;
        }

        var project = editor.getProject();
        if (project == null) {
            LOG.warn("No project available for editor");
            return;
        }

        UUIDFormat format;
        try {
            format = UUIDSettings.getInstance().getFormat();
        } catch (Exception ex) {
            LOG.error("Failed to get UUID settings", ex);
            return;
        }

        String uuid = format.generate();
        var document = editor.getDocument();
        var selection = editor.getSelectionModel();
        int start = selection.getSelectionStart();
        int end = selection.getSelectionEnd();

        WriteCommandAction.runWriteCommandAction(project, () -> {
            if (start != end) {
                document.replaceString(start, end, uuid);
            } else {
                document.insertString(start, uuid);
            }
        });

        LOG.info("Inserted UUID at offset " + start + " (format: " + format.getDisplayName() + ")");
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }
}