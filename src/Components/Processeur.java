package Components;

import Components.Types.Registre;
import Execution.InstructionFonctions.InstructionsArithmetiques;
import Execution.Instructions.Instruction;

import javax.swing.*;
import java.util.HashMap;

public class Processeur
{
    private final Memoire memoire;
    private final Registres registres;

    public Processeur(Memoire memoire,
                      HashMap<Registre, JTextField> registerFields,
                      HashMap<Registre, JLabel> flagsFields)
    {
        registres = new Registres(registerFields, flagsFields);
        this.memoire = memoire;
    }

    public int getRegistre(Registre registre)
    {
        return registres.getRegistre(registre);
    }

    public void setRegistre(Registre registre, int valeur)
    {
        registres.setRegistre(registre, valeur);
    }

    public void executer(HashMap<Integer, Instruction> instructions)
    {
        InstructionsArithmetiques.initialiser(this);
        for (var entry : instructions.entrySet())
        {
            int pc = entry.getKey();
            setRegistre(Registre.PC, pc);

            Instruction instruction = entry.getValue();
            instruction.execute(this);
        }

        var entry = instructions.entrySet().stream().toList().getLast();
        setRegistre(Registre.PC, entry.getKey() + entry.getValue().size());
    }

    public void executer(int pc, Instruction instruction)
    {
        InstructionsArithmetiques.initialiser(this);
        setRegistre(Registre.PC, pc);
        setRegistre(Registre.PC, pc + instruction.size());
        instruction.execute(this);
    }

    public Memoire memoire()
    {
        return memoire;
    }

    public void updateNZ(int valeur)
    {
        int negMask = valeur > 0xFF ? 0x8000 : 0x80;
        setRegistre(Registre.Z, valeur == 0 ? 1 : 0);
        setRegistre(Registre.N, (valeur & negMask) != 0 ? 1 : 0);
    }

    public void reset()
    {
       
        for (Registre registre : Registre.values())
        {
            registres.setRegistre(registre, 0);
        }
    }
}