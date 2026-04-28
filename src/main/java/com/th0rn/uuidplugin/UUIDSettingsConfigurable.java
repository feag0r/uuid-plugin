package com.th0rn.uuidplugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class UUIDSettingsConfigurable implements SearchableConfigurable {

    private JPanel settingsPanel;
    private JCheckBox uppercaseCheckBox;
    private JTextField delimiterField;
    private JTextField bracesField;
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
        delimiterField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updatePreview));
        gbc.gridx = 1;
        gbc.gridy = 1;
        settingsPanel.add(delimiterField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        settingsPanel.add(new JLabel("Braces:"), gbc);

        bracesField = new JTextField(2);
        bracesField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updatePreview));
        gbc.gridx = 1;
        gbc.gridy = 2;
        settingsPanel.add(bracesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 3, 3, 3);
        previewLabel = new JLabel("Preview: 550e8400-e29b-41d4-a716-446655440000");
        settingsPanel.add(previewLabel, gbc);

        return settingsPanel;
    }

    private void updatePreview() {
        if (previewLabel == null) return;
        String delimiter = delimiterField.getText();
        String braces = bracesField.getText();
        boolean upper = uppercaseCheckBox.isSelected();
        previewLabel.setText("Preview: " + UUIDFormat.generate(upper, delimiter, braces));
    }

    @Override
    public void apply() {
        String delimiter = delimiterField.getText();
        if (delimiter.length() > 2) {
            delimiter = delimiter.substring(0, 2);
            delimiterField.setText(delimiter);
        }
        settings.setDelimiter(delimiter);

        String braces = bracesField.getText();
        if (braces.length() > 2) {
            braces = braces.substring(0, 2);
            bracesField.setText(braces);
        }
        settings.setBraces(braces);

        settings.setUppercase(uppercaseCheckBox.isSelected());
    }

    @Override
    public void reset() {
        settings = UUIDSettings.getInstance();
        uppercaseCheckBox.setSelected(settings.isUppercase());
        delimiterField.setText(settings.getDelimiter());
        bracesField.setText(settings.getBraces());
        updatePreview();
    }

    @Override
    public boolean isModified() {
        if (uppercaseCheckBox.isSelected() != settings.isUppercase()) return true;
        if (!delimiterField.getText().equals(settings.getDelimiter())) return true;
        if (!bracesField.getText().equals(settings.getBraces())) return true;
        return false;
    }

    @Override
    public void disposeUIResources() {
        settingsPanel = null;
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
