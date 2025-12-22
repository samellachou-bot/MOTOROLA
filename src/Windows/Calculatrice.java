package Windows;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Calculatrice
{
    public static void show()
    {
        JFrame calcFrame = null;
        String calcTitle = "Calculatrice Hexadécimale";
        for (Frame f : JFrame.getFrames())
        {
            if (calcTitle.equals(f.getTitle()))
            {
                calcFrame = (JFrame)f;
                break;
            }
        }

        if (calcFrame != null && calcFrame.isVisible())
        {
            calcFrame.setVisible(false);
            return;
        }

        calcFrame = new JFrame(calcTitle);
        calcFrame.setSize(420, 160);
        calcFrame.setResizable(false);
        calcFrame.setLayout(new FlowLayout());

        JTextField inputField = new JTextField(22);
        JButton calcButton = new JButton("Calculer");
        JLabel resultLabel = new JLabel("Résultat : ");

        calcFrame.add(new JLabel("Entrer l'opération :"));
        calcFrame.add(inputField);
        calcFrame.add(calcButton);
        calcFrame.add(resultLabel);

        var operations = new HashMap<String, BiFunction<Integer, Integer, Integer>>(Map.of(
                "+", Integer::sum,
                "-", (a, b) -> a - b,
                "*", (a, b) -> a * b,
                "/", (a, b) -> a / b
        ));

        calcButton.addActionListener(ignored -> {
            try
            {
                String expr = inputField.getText().trim();
                expr = expr.replaceAll("\\s+", "").toUpperCase();

                int result = 0;
                boolean isComputed = false;

                String[] operands = expr.split("[+\\-*/]");
                for (String operation : operations.keySet())
                {
                    if (expr.contains(operation))
                    {
                        var operationFunc = operations.get(operation);
                        result = operationFunc.apply(Integer.parseInt(operands[0], 16),
                                                     Integer.parseInt(operands[1], 16));
                        isComputed = true;
                        break;
                    }
                }

                if (!isComputed)
                    result = Integer.parseInt(expr, 16);

                resultLabel.setText("Résultat : " + Integer.toHexString(result).toUpperCase());
            }
            catch (Exception ex)
            {
                resultLabel.setText("Erreur dans l'expression !");
            }
        });

        calcFrame.setVisible(true);
    }
}