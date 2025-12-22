package main;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class Numligne extends JTextArea {

    private JTextArea textArea;

    public Numligne(JTextArea textArea) {
        this.textArea = textArea;
        setEditable(false);
        setBackground(Color.LIGHT_GRAY);
        setFont(textArea.getFont());
        setForeground(Color.BLACK);
        setColumns(4); // largeur pour les numéros
        updateNumbers();

        // Mettre à jour les numéros à chaque modification dans le texte
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateNumbers(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateNumbers(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateNumbers(); }
        });
    }

    private void updateNumbers() {
        int lines = textArea.getLineCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            sb.append(i).append("\n");
        }
        setText(sb.toString());
    }
}