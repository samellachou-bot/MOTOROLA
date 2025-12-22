package Execution;

import Components.Memoire;
import Components.Processeur;
import Components.Types.Registre;
import Execution.Instructions.Instruction;
import Execution.Instructions.ModeAdressage;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Executeur
{
    private final Memoire memoire;
    private final Processeur processeur;

    public Executeur(Processeur processeur, Memoire memoire)
    {
        this.memoire = memoire;
        this.processeur = processeur;
    }

    /**
     * Calcule l'adresse effective pour le mode indexé 6809
     * @param operand ex: ",X", ",X+", ",++X", "5,X", ",--S"
     * @param r tableau des registres
     * @return adresse mémoire effective (16 bits)
     */
    public int calculAdresseIndexee(String operand, int[] r)
    {
        operand = operand.toUpperCase().replace(" ", "");

        boolean pre = false;
        boolean post = false;
        int incDec = 0;
        int offset = 0;

        // ---------------------------
        // 1) Détection pré/post +/-
        // ---------------------------

        if (operand.startsWith(",++")) {
            pre = true;
            incDec = 2;
            operand = operand.substring(3);
        } else if (operand.startsWith(",+")) {
            pre = true;
            incDec = 1;
            operand = operand.substring(2);
        } else if (operand.startsWith(",--")) {
            pre = true;
            incDec = -2;
            operand = operand.substring(3);
        } else if (operand.startsWith(",-")) {
            pre = true;
            incDec = -1;
            operand = operand.substring(2);
        } else if (operand.endsWith("++")) {
            post = true;
            incDec = 2;
            operand = operand.substring(0, operand.length() - 2);
        } else if (operand.endsWith("+")) {
            post = true;
            incDec = 1;
            operand = operand.substring(0, operand.length() - 1);
        } else if (operand.endsWith("--")) {
            post = true;
            incDec = -2;
            operand = operand.substring(0, operand.length() - 2);
        } else if (operand.endsWith("-")) {
            post = true;
            incDec = -1;
            operand = operand.substring(0, operand.length() - 1);
        }

        // ---------------------------
        // 2) Offset (constante ou registre)
        // ---------------------------

        if (operand.contains(",")) {
            String[] p = operand.split(",");

            if (!p[0].isEmpty()) {
                if (p[0].equals("A")) offset = (byte) r[0];
                else if (p[0].equals("B")) offset = (byte) r[1];
                else if (p[0].equals("D")) offset = (short) ((r[0] << 8) | r[1]);
                else offset = (byte) Integer.parseInt(p[0].replace("$", ""), 16);
            }

            operand = "," + p[1];
        }

        // ---------------------------
        // 3) Registre indexé
        // ---------------------------

        int idx;
        if (operand.equals(",X")) idx = 6;
        else if (operand.equals(",Y")) idx = 7;
        else if (operand.equals(",U")) idx = 4;
        else if (operand.equals(",S")) idx = 3;
        else if (operand.equals(",PC")) idx = 2;
        else throw new RuntimeException("Registre indexé invalide");

        // ---------------------------
        // 4) Pré-incr / pré-décr
        // ---------------------------

        if (pre) r[idx] += incDec;

        // ---------------------------
        // 5) Calcul adresse effective
        // ---------------------------

        int addr = (r[idx] + offset) & 0xFFFF;

        // ---------------------------
        // 6) Post-incr / post-décr
        // ---------------------------

        if (post) r[idx] += incDec;

        return addr;
    }
}