package main;
import Compilation.Decodeur;
import Components.Memoire;
import Components.Processeur;
import Components.Types.Registre;
import Controls.MemoireTableModel;
import Execution.Instructions.Instruction;
import Files.FileManager;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actions
{
    private final Memoire ram, rom;
    private final Decodeur decodeur;
    private final Processeur processeur;
    private final UndoManager undoManager;
    private final JTextArea erreurOutput;

    private int ligneCourante = 0;
    private boolean enCours = false;

    private JFrame fntRAM, fntROM;
    private MemoireTableModel romModel;
    private final JTextArea zoneProgramme;

    public Actions(JTextArea zoneProgramme,
                   JTextArea erreurOutput,
                   HashMap<Registre, JLabel> flagsFields,
                   HashMap<Registre, JTextField> registerFields)
    {
        this.erreurOutput = erreurOutput;
        rom = new Memoire(8 * 1024);
        ram = new Memoire(56 * 1024);

        decodeur = new Decodeur(rom);
        processeur = new Processeur(ram, registerFields, flagsFields);
        this.zoneProgramme = zoneProgramme;
        undoManager = new UndoManager();
        zoneProgramme.getDocument().addUndoableEditListener(undoManager);
    }

    // ================= ROM / RAM =================
    public void ouvrirFenetreRAM()
    {
        if (fntRAM == null)
            fntRAM = createMemoryWindow("RAM", ram);

        toggle(fntRAM);
    }

    public void ouvrirFenetreROM()
    {
        if (fntROM == null)
            fntROM = createMemoryWindow("ROM", rom);

        toggle(fntROM);
    }

    private void toggle(JFrame f)
    {
        f.setVisible(!f.isVisible());
    }

    private JFrame createMemoryWindow(String title, Memoire memoire)
    {
        JFrame frame = new JFrame(title);
        frame.setSize(500,400);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        var header = new String[]{ "Adresse", "Valeur" };

        if(title.equals("ROM"))
        {
            romModel = new MemoireTableModel(memoire, false);
            romModel.setColumnIdentifiers(header);
            JTable tableROM = new JTable(romModel);
            frame.add(new JScrollPane(tableROM));
        }
        else
        {
            var ramModel = new MemoireTableModel(memoire, true);
            ramModel.setColumnIdentifiers(header);
            JTable tableRAM = new JTable(ramModel);
            frame.add(new JScrollPane(tableRAM));
        }

        frame.setLocationRelativeTo(null);
        return frame;
    }

    private void actualiserROM()
    {
        if (romModel != null)
            romModel.fireTableDataChanged();
    }

    public void programme()
    {
        ligneCourante = 0;
        processeur.setRegistre(Registre.PC, 0); // PC au début
        processeur.reset();
    }

    public void undo()
    {
        if (undoManager.canUndo())
        {
            try {
                undoManager.undo();
            } catch (CannotUndoException ignored) {

            }
        }
    }

    public void redo()
    {
        if (undoManager.canRedo())
        {
            try {
                undoManager.redo();
            } catch (CannotRedoException ignored) {

            }
        }
    }

    public void manipulation()
    {
        ligneCourante = 0;
        erreurOutput.setText("");
        instructions = decodeur.decoder(zoneProgramme.getText()).entrySet().stream().toList();
    }

    // ================= Load / Save =================
    public void load()
    {
        String text = FileManager.load();
        zoneProgramme.setText(text);
        ligneCourante = 0;
    }

    public void save()
    {
        String text = zoneProgramme.getText();
        FileManager.save(text);
    }

    List<Map.Entry<Integer, Instruction>> instructions;
    // ================= Exécution =================
    public void pasAPas()
    {
        try
        {
            if (!enCours || instructions == null || instructions.isEmpty())
            {
                processeur.reset();
                manipulation();
            }

            if (instructions.isEmpty())
                return;

            if (ligneCourante >= instructions.size())
            {
                enCours = false;
                return;
            }
            var instr = instructions.get(ligneCourante);

            int pc = instr.getKey();
            processeur.executer(pc, instr.getValue());

            surlignerLigne(ligneCourante);
            actualiserROM();

            ligneCourante++;
            enCours = true;

        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            enCours = false;
        }
    }


    public Processeur getProcesseur()
    {
        return processeur;
    }
    
    public void execute()
    {
        processeur.reset();
        erreurOutput.setText("");
        String code = zoneProgramme.getText();
        var instructions = decodeur.decoder(code);

        if (!instructions.isEmpty())
            processeur.executer(instructions);
    }

    private void surlignerLigne(int l)
    {
        try
        {
            int s = zoneProgramme.getLineStartOffset(l);
            int e = zoneProgramme.getLineEndOffset(l);
            zoneProgramme.select(s, e);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
}