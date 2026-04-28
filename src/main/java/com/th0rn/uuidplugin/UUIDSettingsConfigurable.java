package com.th0rn.uuidplugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class UUIDSettingsConfigurable implements SearchableConfigurable {

    private JPanel settingsPanel;
    private JCheckBox uppercaseCheckBox;
    private JTextField delimiterField;
    private JTextField leftBraceField;
    private JTextField rightBraceField;
    private JLabel previewLabel;
    private UUIDSettings settings;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "UUID Plugin";
    }

    @NotNull
    @Override
    public String getId() {
        return "com.th0rn.uuidplugin.settings";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        var gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);

        uppercaseCheckBox = new JCheckBox("Upper case");
        uppercaseCheckBox.addItemListener(e -> updatePreview());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        settingsPanel.add(uppercaseCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        settingsPanel.add(new JLabel("Delimiter:"), gbc);

        delimiterField = new JTextField(2);
        addLengthLimit(delimiterField, 2);
        delimiterField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updatePreview));
        gbc.gridx = 1;
        gbc.gridy = 1;
        settingsPanel.add(delimiterField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        settingsPanel.add(new JLabel("Left brace:"), gbc);

        leftBraceField = new JTextField(2);
        addLengthLimit(leftBraceField, 2);
        leftBraceField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updatePreview));
        gbc.gridx = 1;
        gbc.gridy = 2;
        settingsPanel.add(leftBraceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        settingsPanel.add(new JLabel("Right brace:"), gbc);

        rightBraceField = new JTextField(2);
        addLengthLimit(rightBraceField, 2);
        rightBraceField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updatePreview));
        gbc.gridx = 1;
        gbc.gridy = 3;
        settingsPanel.add(rightBraceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 3, 3, 3);
        previewLabel = new JLabel("Preview: 550e8400-e29b-41d4-a716-446655440000");
        settingsPanel.add(previewLabel, gbc);

        return settingsPanel;
    }

    private void updatePreview() {
        if (previewLabel == null) return;
        String delimiter = delimiterField.getText();
        String leftBrace = leftBraceField.getText();
        String rightBrace = rightBraceField.getText();
        boolean upper = uppercaseCheckBox.isSelected();
        previewLabel.setText("Preview: " + UUIDFormat.generate(upper, delimiter, leftBrace, rightBrace));
    }

    @Override
    public void apply() {
        String delimiter = delimiterField.getText();
        if (delimiter.length() > 2) {
            delimiter = delimiter.substring(0, 2);
            delimiterField.setText(delimiter);
        }
        settings.setDelimiter(delimiter);

        String leftBrace = leftBraceField.getText();
        if (leftBrace.length() > 2) {
            leftBrace = leftBrace.substring(0, 2);
            leftBraceField.setText(leftBrace);
        }
        settings.setLeftBrace(leftBrace);

        String rightBrace = rightBraceField.getText();
        if (rightBrace.length() > 2) {
            rightBrace = rightBrace.substring(0, 2);
            rightBraceField.setText(rightBrace);
        }
        settings.setRightBrace(rightBrace);

        settings.setUppercase(uppercaseCheckBox.isSelected());
    }

    @Override
    public void reset() {
        settings = UUIDSettings.getInstance();
        uppercaseCheckBox.setSelected(settings.isUppercase());
        delimiterField.setText(settings.getDelimiter());
        leftBraceField.setText(settings.getLeftBrace());
        rightBraceField.setText(settings.getRightBrace());
        updatePreview();
    }

    @Override
    public boolean isModified() {
        if (uppercaseCheckBox.isSelected() != settings.isUppercase()) return true;
        if (!delimiterField.getText().equals(settings.getDelimiter())) return true;
        if (!leftBraceField.getText().equals(settings.getLeftBrace())) return true;
        if (!rightBraceField.getText().equals(settings.getRightBrace())) return true;
        return false;
    }

    @Override
    public void disposeUIResources() {
        settingsPanel = null;
    }

    private static void addLengthLimit(JTextField field, int maxLen) {
        var doc = (AbstractDocument) field.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String str, AttributeSet attr) throws BadLocationException {
                if ((fb.getDocument().getLength() + str.length()) <= maxLen) {
                    super.insertString(fb, offset, str, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
                if ((fb.getDocument().getLength() - length + str.length()) <= maxLen) {
                    super.replace(fb, offset, length, str, attrs);
                }
            }
        });
    }

    private static class SimpleDocumentListener implements DocumentListener {
        private final Runnable callback;

        SimpleDocumentListener(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            callback.run();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            callback.run();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            callback.run();
        }
    }
}