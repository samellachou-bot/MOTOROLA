package Execution.InstructionFonctions;

import Components.Processeur;
import Components.Types.Registre;

public class InstructionsArithmetiques
{
    public static Processeur processeur;
    public static void initialiser(Processeur p)
    {
        processeur = p;
        InstructionsLoadStore.initialiser(p);
    }

    public static void add(Registre registre, int valeur)
    {
        addc(registre, valeur, false);
    }

    public static void adc(Registre registre, int valeur)
    {
        addc(registre, valeur, true);
    }

    public static void addc(Registre registre, int valeur, boolean carry)
    {
        boolean isValidReg = switch (registre) {
            case A, B, D -> true;
            default -> false;
        };

        if (!isValidReg)
        {
            System.err.println("Registre invalide");
            return;
        }

        int valRegistre = processeur.getRegistre(registre);

        int c = 0;
        if (carry)
            c = processeur.getRegistre(Registre.C);

        int res = valRegistre + valeur + c;
        int mask = registre.est8Bits() ? 0xFF : 0xFFFF;
        int negMask = registre.est8Bits() ? 0x80 : 0x8000;
        res &= mask;

        processeur.setRegistre(registre, res);
        processeur.setRegistre(Registre.Z, res == 0 ? 1 : 0);
        processeur.setRegistre(Registre.N, (res & negMask) != 0 ? 1 : 0);
        processeur.setRegistre(Registre.V, 0); // Supposons pas de debordement

        int halfCarrySomme = (valRegistre & 0xF) + (valeur & 0xF) + c;
        processeur.setRegistre(Registre.H, halfCarrySomme > 0xF ? 1 : 0);
        processeur.setRegistre(Registre.C, valRegistre + valeur + c > mask ? 1 : 0);
    }
}