package Controls;

import Components.Memoire;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MemoireTableModel extends DefaultTableModel
{
    private final Memoire memoire;
    private final boolean editable;

    public MemoireTableModel(Memoire memoire, boolean editable)
    {
        this.memoire = memoire;
        this.editable = editable;
    }

    public int getRowCount()
    {
        return 256;
    }

    public int getColumnCount()
    {
        return 2;
    }

    public Object getValueAt(int row, int col)
    {
        if(col == 0)
            return String.format("%04X", row);

        return String.format("%02X", memoire.lire(row));
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        if (editable)
            return column == 1;

        JOptionPane.showMessageDialog(null,
                             "Non modifiable", "Valeur invalide", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        if (!isInt(aValue.toString()))
        {
            JOptionPane.showMessageDialog(null,
                                 "Valeur invalide", "Valeur invalide", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int nouveauValeur = Integer.parseInt(aValue.toString(), 16);
        if (nouveauValeur < 0 || nouveauValeur > 0xFF)
        {
            JOptionPane.showMessageDialog(null,
                                 "Valeur hexadecimale invalide", "Valeur invalide", JOptionPane.ERROR_MESSAGE);
            return;
        }

        memoire.ecrire(row, nouveauValeur);
        super.setValueAt(aValue, row, column);
    }

    private boolean isInt(String str)
    {
        try
        {
            Integer.parseInt(str, 16);
            return true;
        }
        catch (Exception ignored)
        {

        }

        return false;
    }
}