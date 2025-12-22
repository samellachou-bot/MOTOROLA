package Execution.InstructionFonctions;

import Components.Processeur;
import Components.Types.Registre;

public class InstructionsLoadStore 
{
    public static Processeur processeur;
    public static void initialiser(Processeur p)
    {
        processeur = p;
    }

    public static void loadAcc(Registre acc, int value)
    {
        boolean isValidAcc = switch (acc) {
            case A, B, D -> true;
            default -> false;
        };
        if (!isValidAcc)
        {
            return;
        }

        int mask = acc.est8Bits() ? 0xFF : 0xFFFF;
        processeur.setRegistre(acc, value & mask);
        processeur.setRegistre(Registre.V, 0);
        processeur.setRegistre(Registre.C, 0);
        processeur.updateNZ(value);
    }

    public static void stoAcc(Registre acc, int address)
    {
        boolean isValidAcc = switch (acc) {
            case A, B, D -> true;
            default -> false;
        };
        if (!isValidAcc)
        {
            return;
        }

        int value = processeur.getRegistre(acc);
        processeur.memoire().ecrire(address, value);

        processeur.setRegistre(Registre.V, 0);
        processeur.setRegistre(Registre.C, 0);
        processeur.updateNZ(value);
    }

    public static void loadSX(Registre reg, int value)
    {
        boolean isValidReg = switch (reg) {
            case X, Y, S, U -> true;
            default -> false;
        };
        if (!isValidReg)
            return;

        processeur.setRegistre(reg, value);

        processeur.setRegistre(Registre.V, 0);
        processeur.setRegistre(Registre.C, 0);

        // Update N/Z flags based on the high byte
        processeur.updateNZ(value >> 8);
    }

    public static void lea(Registre reg, int offset)
    {
        boolean isValidReg = switch (reg) {
            case X, Y, S, U -> true;
            default -> false;
        };
        if (!isValidReg)
            return;


        int effectiveAddress = processeur.getRegistre(reg);
        effectiveAddress = (effectiveAddress + offset) & 0xFFFF;
        processeur.setRegistre(reg, effectiveAddress);

        if (reg == Registre.X || reg == Registre.Y)
        {
            processeur.setRegistre(Registre.Z, effectiveAddress == 0 ? 1 : 0);
        }
    }
    
    public static void stoSX(Registre reg, int address)
    {
        boolean isValidReg = switch (reg) {
            case X, Y, S, U -> true;
            default -> false;
        };
        if (!isValidReg)
            return;

        int regValue = processeur.getRegistre(reg);

        processeur.memoire().ecrire(address, regValue >> 8);
        processeur.memoire().ecrire(address + 1, regValue & 0xFF);

        processeur.setRegistre(Registre.V, 0);
        processeur.setRegistre(Registre.C, 0);
        // Update N/Z flags based on the high byte
        processeur.updateNZ(regValue >> 8);;
    }
}