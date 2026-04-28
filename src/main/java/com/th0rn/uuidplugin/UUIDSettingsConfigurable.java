package com.th0rn.uuidplugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class UUIDSettingsConfigurable implements SearchableConfigurable {

    private JPanel settingsPanel;
    private JComboBox<UUIDFormat> formatComboBox;
    private UUIDSettings settings;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "UUID Plugin";
    }

    @NotNull
    @Override
    public String getId() {
        return "UUIDPluginSettings";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsPanel = new JPanel(new BorderLayout(10, 10));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 0, 10));

        JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formatPanel.add(new JLabel("UUID Format:"));

        formatComboBox = new JComboBox<>(UUIDFormat.values());
        formatComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof UUIDFormat uuidFormat) {
                    setText(uuidFormat.getDisplayName());
                }
                return this;
            }
        });
        formatPanel.add(formatComboBox);

        mainPanel.add(formatPanel);

        settingsPanel.add(mainPanel, BorderLayout.CENTER);
        return settingsPanel;
    }

    @Override
    public void apply() {
        if (formatComboBox.getSelectedItem() instanceof UUIDFormat selectedFormat) {
            settings.setFormat(selectedFormat);
        }
    }

    @Override
    public void reset() {
        settings = UUIDSettings.getInstance();
        for (int i = 0; i < formatComboBox.getItemCount(); i++) {
            if (formatComboBox.getItemAt(i) == settings.getFormat()) {
                formatComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public boolean isModified() {
        UUIDSettings currentSettings = UUIDSettings.getInstance();
        for (int i = 0; i < formatComboBox.getItemCount(); i++) {
            if (formatComboBox.getItemAt(i) == currentSettings.getFormat()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void disposeUIResources() {
        settingsPanel = null;
    }

    @Override
    public String getHelpTopic() {
        return "UUIDPluginSettings";
    }
}
