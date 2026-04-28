package com.th0rn.uuidplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.jetbrains.annotations.NotNull;

public class UUIDInsertAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        UUIDSettings settings = UUIDSettings.getInstance();
        UUIDFormat format = settings.getFormat();
        String uuid = format.generate();

        SelectionModel selectionModel = editor.getSelectionModel();
        int selectionStart = selectionModel.getSelectionStart();
        int selectionEnd = selectionModel.getSelectionEnd();

        CommandProcessor.getInstance().executeCommand(
            editor.getProject(),
            () -> {
                if (selectionStart != selectionEnd) {
                    editor.getDocument().replaceString(selectionStart, selectionEnd, uuid);
                } else {
                    editor.getDocument().insertString(selectionStart, uuid);
                }
            },
            "Insert UUID",
            null
        );
    }
}
