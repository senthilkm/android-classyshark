/*
 * Copyright 2015 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.classyshark.ui.viewer.toolbar;

import com.google.classyshark.ui.ColorScheme;
import com.google.classyshark.ui.viewer.ClassySharkPanel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * toolbar = buttons + command line
 */
public class ToolBar extends JToolBar {

    private final JTextField typingArea;
    private final ClassySharkPanel classySharkPanel;

    private JButton openBtn;
    private JButton viewBtn;
    private JButton backBtn;
    private JButton infoBtn;
    private JToggleButton leftPanelToggleBtn;

    public ToolBar(final ClassySharkPanel classySharkPanel) {
        super();
        UIManager.put("ToolBar.background", ColorScheme.BACKGROUND);
        UIManager.put("ToolBar.foreground", ColorScheme.BACKGROUND);

        UIManager.put("Button.background", ColorScheme.BACKGROUND);
        UIManager.put("Button.foreground", ColorScheme.WHITE);

        Font f = new Font("Menlo", Font.PLAIN, 18);
        UIManager.put("Button.font", f);

        this.classySharkPanel = classySharkPanel;

        typingArea = buildTypingArea();
        openBtn = buildOpenButton();
        backBtn = buildBackButton();
        viewBtn = buildViewButton();
        infoBtn = buildInfoButton();
        leftPanelToggleBtn = buildLeftPanelToggleButton();

        this.setBackground(ColorScheme.BLACK);

        add(leftPanelToggleBtn);
        add(openBtn);
        add(backBtn);
        add(viewBtn);
        add(typingArea);
        add(infoBtn);

        setFloatable(false);

        Border roundedBorder = new LineBorder(ColorScheme.BLACK, 5);
        setBorder(roundedBorder);
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
    }

    public void addKeyListenerToTypingArea(ClassySharkPanel classySharkPanel) {
        typingArea.addKeyListener(classySharkPanel);
    }

    public void setTypingArea() {
        typingArea.setBackground(ColorScheme.LIGHT_GRAY);

        Font typingAreaFont = new Font("Menlo", Font.PLAIN, 18);
        typingArea.setFont(typingAreaFont);
        typingArea.setForeground(ColorScheme.FOREGROUND_CYAN);

        setTypingAreaCaret();
    }

    public void setTypingAreaCaret() {
        int len = typingArea.getDocument().getLength();
        typingArea.setCaretPosition(len);
        typingArea.setCaretColor(ColorScheme.FOREGROUND_CYAN);
    }

    public String getText() {
        return typingArea.getText();
    }

    public void setText(String text) {
        typingArea.setText(text);
    }

    public void activateNavigationButtons() {
        viewBtn.setEnabled(true);
        backBtn.setEnabled(true);
    }

    private JTextField buildTypingArea() {
        final JTextField result = new JTextField(50) {
            @Override
            public void setBorder(Border border) {
            }
        };

        result.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (result.getSelectedText() != null) {
                    String textToDelete = typingArea.getSelectedText();
                    String selectedLine = result.getText().substring(0,
                            result.getText().lastIndexOf(textToDelete));

                    result.setText(selectedLine);
                    classySharkPanel.onChangedTextFromTypingArea(result.getText());
                }
            }
        });

        return result;
    }

    private JButton buildOpenButton() {
        JButton result = new JButton("Open");

        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classySharkPanel.openArchive();
            }
        });

        result.setBorderPainted(false);
        result.setFocusPainted(true);
        result.setForeground(ColorScheme.WHITE);
        result.setBackground(ColorScheme.BLACK);

        return result;
    }

    private JButton buildBackButton() {
        JButton result = new JButton(" <== ");

        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classySharkPanel.onGoBackPressed();
            }
        });

        result.setBorderPainted(false);
        result.setFocusPainted(true);
        result.setForeground(ColorScheme.FOREGROUND_YELLOW);
        result.setBackground(ColorScheme.BLACK);
        result.setEnabled(false);

        return result;
    }

    private JButton buildViewButton() {
        JButton result = new JButton(" ==> ");

        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classySharkPanel.onViewTopClassPressed();
            }
        });

        result.setBorderPainted(false);
        result.setFocusPainted(true);
        result.setForeground(ColorScheme.FOREGROUND_YELLOW);
        result.setBackground(ColorScheme.BLACK);
        result.setEnabled(false);

        return result;
    }

    private JButton buildInfoButton() {
        JButton result = new JButton(" ? ");

        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classySharkPanel.onShowInfoPressed();
            }
        });

        result.setBorderPainted(false);
        result.setFocusPainted(true);
        result.setForeground(ColorScheme.WHITE);
        result.setBackground(ColorScheme.BLACK);

        return result;
    }

    private JToggleButton buildLeftPanelToggleButton() {
        final JToggleButton jToggleButton = new JToggleButton("\u2592", true);
        jToggleButton.setBorderPainted(false);
        jToggleButton.setFocusPainted(true);
        jToggleButton.setForeground(ColorScheme.FOREGROUND_YELLOW);
        jToggleButton.setBackground(ColorScheme.BLACK);
        jToggleButton.setFont(new Font("Menlo", Font.PLAIN, 18));
        jToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classySharkPanel.onChangeLeftPaneVisibility(jToggleButton.isSelected());
            }
        });
        return jToggleButton;
    }
}
