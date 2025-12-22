package main;
import Components.Types.Registre;
import Windows.Calculatrice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static Components.Types.Registre.*;

public class Fenetre extends JFrame
{
    private Actions actions;
    private static JTextArea errorOutput;
    private HashMap<Registre, JLabel> flagsFields;
    private HashMap<Registre, JTextField> registerFields;

    public Fenetre()
    {
        setTitle("Simulateur 6809");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5,5));

        /* ================= PROGRAMME ================= */
        JTextArea zoneProgramme = new JTextArea();
        zoneProgramme.setFont(new Font("Monospaced", Font.PLAIN, 14));
        Numligne lineNumbers = new Numligne(zoneProgramme);
        JScrollPane scrollProgramme = new JScrollPane(zoneProgramme);
        scrollProgramme.setRowHeaderView(lineNumbers);
        scrollProgramme.setBorder(BorderFactory.createTitledBorder("Programme"));

        /* ================= ERREURS ================= */
        errorOutput = new JTextArea();
        errorOutput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        errorOutput.setLineWrap(true);
        errorOutput.setEditable(false);
        errorOutput.setForeground(Color.RED);
        errorOutput.setBorder(BorderFactory.createBevelBorder(1, Color.BLACK, Color.BLACK));

        JScrollPane scrollErrorPane = new JScrollPane(errorOutput);
        scrollErrorPane.setPreferredSize(new Dimension(errorOutput.getPreferredSize().width, 150));
        scrollErrorPane.setBorder(BorderFactory.createTitledBorder("Erreurs"));

        JPanel zonnesPanel = new JPanel(new BorderLayout(5,5));
        zonnesPanel.add(scrollProgramme, BorderLayout.CENTER);
        zonnesPanel.add(scrollErrorPane, BorderLayout.SOUTH);

        JPanel centre = new JPanel(new BorderLayout(5,5));
        centre.add(zonnesPanel, BorderLayout.CENTER);
        centre.add(creerPanelRegistres(), BorderLayout.EAST);

        add(creerBoutonsAction(zoneProgramme), BorderLayout.NORTH);
        add(centre, BorderLayout.CENTER);
    }

    private JPanel creerBoutonsAction(JTextArea zoneProgramme)
    {
        actions = new Actions(zoneProgramme, errorOutput, flagsFields, registerFields);
        JPanel panelBoutons = new JPanel(new GridLayout(1, 11, 5,5));
        String[] noms = { "programme", "fleche2", "fleche1",
                          "manipulation", "Pas_à_pas", "execute",
                          "ram", "rom", "load", "save", "calculatrice"};

        for (String nom : noms)
        {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/moto/images/" + nom + ".png")));
            Image img = icon.getImage().getScaledInstance(22,22, Image.SCALE_SMOOTH);

            JButton boutton = new JButton(new ImageIcon(img));
            boutton.setBorderPainted(false);
            boutton.setContentAreaFilled(false);
            boutton.setFocusPainted(false);

            String tooltip = switch (nom) {
                case "programme" -> "Effacer tous";
                case "fleche2" -> "Undo";
                case "fleche1" -> "Redo";
                case "manipulation" -> "Compiler";
                case "Pas_à_pas" -> "Pas à pas";
                case "execute" -> "Executer";
                case "ram" -> "Afficher RAM";
                case "rom" -> "Afficher ROM";
                case "load" -> "Ouvrir fichier";
                case "save" -> "Sauvegarder";
                default -> "Calculatrice";
            };

            boutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            boutton.setToolTipText(tooltip);

            boutton.addActionListener(ignored -> {
                switch (nom) {
                    case "programme" -> actions.programme();
                    case "fleche2" -> actions.undo();
                    case "fleche1" -> actions.redo();
                    case "manipulation" -> actions.manipulation();
                    case "Pas_à_pas" -> actions.pasAPas();
                    case "execute" -> actions.execute();
                    case "ram" -> actions.ouvrirFenetreRAM();
                    case "rom" -> actions.ouvrirFenetreROM();
                    case "load" -> actions.load();
                    case "save" -> actions.save();
                    case "calculatrice" -> Calculatrice.show();
                }
            });

            panelBoutons.add(boutton);
        }

        return panelBoutons;
    }

    /* ================= REGISTRES ================= */
    private JPanel creerPanelRegistres()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registres"));
        panel.setPreferredSize(new Dimension(450,0));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        flagsFields = new HashMap<>();
        Registre[] flags = { E, F, H, I, N, Z, V, C };
        JPanel panelFlags = new JPanel(new GridLayout(1,7,2,2));

        for (Registre flag : flags)
        {
            JLabel flagLabel = new JLabel("0", SwingConstants.CENTER);
            flagLabel.setOpaque(true);
            flagLabel.setBackground(Color.WHITE);
            flagLabel.setFont(new Font("Monospaced", Font.BOLD,16));

            flagsFields.put(flag, flagLabel);

            JLabel flagName = new JLabel(flag.toString(), SwingConstants.CENTER);
            flagName.setFont(new Font("Monospaced", Font.BOLD,16));

            JPanel panelFlag = new JPanel(new GridLayout(2,1, 0, 5));
            panelFlag.add(flagLabel);
            panelFlag.add(flagName);

            panelFlags.add(panelFlag);
        }

        panel.add(panelFlags, gbc(c,0, y++,3));

        List<Registre> registres = List.of(PC, A, B, D, X, Y, S, U, DP);
        registerFields = new HashMap<>();

        int x = 0, yreg = y;
        for (Registre registre : registres)
        {
            y = yreg;
            int w = 1;
            if (registre == registres.getFirst())
            {
                w = 3;
                yreg += 2;
            }

            panel.add(new JLabel(registre.toString(), SwingConstants.CENTER), gbc(c, x, y++, w));
            String champText = registre.est8Bits() ? "00" : "0000";
            JTextField registreLabel = champ(champText);

            if (registre == Registre.PC)
                registreLabel.setEditable(false);

            addActionListener(registreLabel, registre);

            registerFields.put(registre, registreLabel);
            panel.add(registreLabel, gbc(c, x, y, w));
            if (registre != registres.getFirst())
            {
                x++;
            }
            if (x % 2 == 0)
            {
                x = 0;
                yreg += 2;
            }
        }

        c.weighty = 1;
        return panel;
    }

    private void addActionListener(JTextField registreLabel, Registre registre)
    {
        registreLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String text = registreLabel.getText();
                var processeur = actions.getProcesseur();
                String format = registre.est8Bits() ? "%02X" : "%04X";

                if (shouldNotAllow(text))
                {
                    Toolkit.getDefaultToolkit().beep();
                    int val = processeur.getRegistre(registre);
                    registreLabel.setText(String.format(format, val));
                    return;
                }

                int val = Integer.parseInt(text, 16);
                processeur.setRegistre(registre, val);
            }

            private boolean shouldNotAllow(String text)
            {
                int maxDigits = registre.est8Bits() ? 2 : 4;
                return !text.matches("[0-9a-fA-F]{0," + maxDigits + "}");
            }
        });
    }

    private GridBagConstraints gbc(GridBagConstraints c, int x, int y, int w)
    {
        GridBagConstraints g = (GridBagConstraints)c.clone();
        g.gridx = x; g.gridy = y; g.gridwidth = w; g.weightx = 1;
        g.fill = GridBagConstraints.BOTH;
        return g;
    }

    private JTextField champ(String texte)
    {
        JTextField t = new JTextField(texte, 12);
        t.setHorizontalAlignment(JTextField.CENTER);
        t.setFont(new Font("Monospaced", Font.BOLD,16));
        return t;
    }

    static class Output extends PrintStream
    {
        public Output()
        {
            super(System.out, true); // autoFlush = true
        }

        @Override
        public void println(String x)
        {
            errorOutput.append(" " + x + "\n");
        }

        @Override
        public void print(String s)
        {
            errorOutput.append(s);
        }

        // Optional: Override other print/println methods for full coverage
        @Override
        public void println(Object obj) {
            println(String.valueOf(obj));
        }

        @Override
        public void println() {
            println("\n");
        }
    }

    public static void main(String [] args)
    {
        var outputConsole = new Output();
        System.setOut(outputConsole);
        SwingUtilities.invokeLater(() -> new Fenetre().setVisible(true));
    }
}