package com.th0rn.uuidplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

public class UUIDInsertAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(UUIDInsertAction.class);

    public UUIDInsertAction() {
        super("Insert UUID", "Insert UUID at current cursor position", null);
    }

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

        var document = editor.getDocument();
        var carets = editor.getCaretModel().getAllCarets();

        record Op(int start, int end, String uuid) {}

        var ops = carets.stream()
                .map(caret -> new Op(caret.getSelectionStart(), caret.getSelectionEnd(), format.generate()))
                .sorted((a, b) -> Integer.compare(b.start, a.start))
                .toList();

        WriteCommandAction.runWriteCommandAction(project, () -> {
            for (var op : ops) {
                if (op.start != op.end) {
                    document.replaceString(op.start, op.end, op.uuid);
                } else {
                    document.insertString(op.start, op.uuid);
                }
            }
        });

        LOG.info("Inserted " + ops.size() + " UUID(s) (format: " + format.getDisplayName() + ")");
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }
}