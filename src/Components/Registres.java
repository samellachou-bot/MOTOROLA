package Components;

import Components.Types.Registre;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Registres
{
    private int A, B, X, Y, U, S, PC, DP, CC;
    private final List<Registre> flags;

    private final HashMap<Registre, JLabel> flagsFields;
    private final HashMap<Registre, JTextField> registerFields;

    public Registres(HashMap<Registre, JTextField> registerFields,
                     HashMap<Registre, JLabel> flagsFields)
    {
        this.registerFields = registerFields;
        this.flagsFields = flagsFields;
        flags = List.of(
                Registre.E, Registre.F,
                Registre.H, Registre.I,
                Registre.N, Registre.Z,
                Registre.V, Registre.C
            );
    }

    public int getRegistre(Registre registre)
    {
        return switch (registre)
        {
            case S -> S;
            case U -> U;
            case X -> X;
            case Y -> Y;
            case D -> (A << 8) | B;
            case PC -> PC;
            case A -> A;
            case B -> B;
            case DP -> DP;
            case CC -> CC;
            case E -> (CC >> 7) & 1;
            case F -> (CC >> 6) & 1;
            case H -> (CC >> 5) & 1;
            case I -> (CC >> 4) & 1;
            case N -> (CC >> 3) & 1;
            case Z -> (CC >> 2) & 1;
            case V -> (CC >> 1) & 1;
            default -> CC & 1;
        };
    }

    public void setRegistre(Registre register, int regValue)
    {
        regValue &= 0xFFFF;

        switch (register)
        {
            case S:
                S = regValue;
                break;
            case U:
                U = regValue;
                break;
            case X:
                X = regValue;
                break;
            case Y:
                Y = regValue;
                break;
            case D:
                A = regValue >> 8;
                B = regValue & 0xFF;
                break;
            case PC:
                PC = regValue;
                break;
            case A:
                regValue &= 0xFF;
                A = regValue;
                break;
            case B:
                regValue &= 0xFF;
                B = regValue;
                break;
            case DP:
                DP = regValue;
                break;
            case CC:
                regValue &= 0xFF;
                CC = regValue;
                break;
            case E:
                CC = modifyBit(CC, 7, regValue);
                break;
            case F:
                CC = modifyBit(CC, 6, regValue);
                break;
            case H:
                CC = modifyBit(CC, 5, regValue);
                break;
            case I:
                CC = modifyBit(CC, 4, regValue);
                break;
            case N:
                CC = modifyBit(CC, 3, regValue);
                break;
            case Z:
                CC = modifyBit(CC, 2, regValue);
                break;
            case V:
                CC = modifyBit(CC, 1, regValue);
                break;
            default:
                CC = modifyBit(CC, 0, regValue);
                break;
        }

        var registreField = registerFields.get(register);
        var flagField = flagsFields.get(register);

        if (registreField != null)
        {
            String format = register.est8Bits() ? "%02X" : "%04X";
            registreField.setText(String.format(format, regValue));

            if (register == Registre.A || register == Registre.B)
            {
                registreField = registerFields.get(Registre.D);
                registreField.setText(String.format("%04X", getRegistre(Registre.D)));
            }
            else if (register == Registre.D)
            {
                registreField = registerFields.get(Registre.A);
                registreField.setText(String.format("%02X", getRegistre(Registre.A)));

                registreField = registerFields.get(Registre.B);
                registreField.setText(String.format("%02X", getRegistre(Registre.B)));
            }
            else if (register == Registre.CC)
            {
                for (var flagEntry : flagsFields.entrySet())
                {
                    int flagValue = getRegistre(flagEntry.getKey());
                    flagField = flagEntry.getValue();

                    flagField.setText(Integer.toString(flagValue));
                    flagField.setBackground(flagValue == 1 ? Color.GREEN : Color.WHITE);
                }
            }
        }
        else if (flagField != null)
        {
            flagField.setText(Integer.toString(regValue));
            flagField.setBackground(regValue == 1 ? Color.GREEN : Color.WHITE);
        }
    }

    public List<Registre> getFlags()
    {
        return flags;
    }

    private static int modifyBit(int number, int bitPosition, int bit)
    {
        bit &= 1;
        return (bit == 0) ? (number & ~(1 << bitPosition)) : (number | (1 << bitPosition));
    }
}