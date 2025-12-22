package Components;

import javax.swing.*;
import java.util.Arrays;

public class Memoire
{
    private final int[] memoire;

    public Memoire(int size)
    {
        memoire = new int[size];
    }

    public int lire(int adresse)
    {
        if (adresse >= 0 && adresse <= memoire.length)
            return memoire[adresse];

        return -1;
    }

    public void ecrire(int adresse, int valeur)
    {
        valeur &= 0xFFFF;
        if (adresse < 0 || adresse >= memoire.length)
            return;

        if (valeur > 0xFF && adresse < memoire.length - 1)
        {
            memoire[adresse] = valeur << 8;
            memoire[adresse + 1] = valeur & 0xFF;
        }
        else
        {
            memoire[adresse] = valeur & 0xFF;
        }
    }

    public void reset()
    {
        Arrays.fill(memoire, 0);
    }
}