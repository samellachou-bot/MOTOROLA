package Execution.Instructions;

import Components.Processeur;
import Components.Types.Registre;
import Execution.InstructionFonctions.InstructionsArithmetiques;
import Execution.InstructionFonctions.InstructionsLoadStore;

public record Instruction(String mnemonic, ModeAdressage mode, int size,
                          int val, Registre deplacementReg, Registre modeIndexReg)
{
    public void execute(Processeur processeur)
    {
        String mnemo = mnemonic.toUpperCase();
        switch (mnemo)
        {
            case "ADDA", "ADDB", "ADDD" -> {
                Registre target = switch (mnemo) {
                    case "ADDA" -> Registre.A;
                    case "ADDB" -> Registre.B;
                    default -> Registre.D;
                };

                InstructionsArithmetiques.add(target, lireValeur(processeur));
            }
            case "ADCA", "ADCB" -> {
                Registre target = switch (mnemo) {
                    case "ADCA" -> Registre.A;
                    default -> Registre.B;
                };

                InstructionsArithmetiques.adc(target, lireValeur(processeur));
            }
            case "LDA", "LDB", "LDD" -> {
                Registre target = switch (mnemo) {
                    case "LDA" -> Registre.A;
                    case "LDB" -> Registre.B;
                    default -> Registre.D;
                };

                InstructionsLoadStore.loadAcc(target, lireValeur(processeur));
            }
            case "LDX", "LDY", "LDS", "LDU" -> {
                Registre target = switch (mnemo) {
                    case "LDX" -> Registre.X;
                    case "LDY" -> Registre.Y;
                    case "LDS" -> Registre.S;
                    default -> Registre.U;
                };

                InstructionsLoadStore.loadSX(target, lireValeur(processeur));
            }
            case "STA", "STB", "STD" -> {
                Registre target = switch (mnemo) {
                    case "STA" -> Registre.A;
                    case "STB" -> Registre.B;
                    default -> Registre.D;
                };

                InstructionsLoadStore.stoAcc(target, lireAdresse(processeur));
            }
            case "STX", "STY", "STS", "STU" -> {
                Registre target = switch (mnemo) {
                    case "STX" -> Registre.X;
                    case "STY" -> Registre.Y;
                    case "STS" -> Registre.S;
                    default -> Registre.U;
                };

                InstructionsLoadStore.stoSX(target, lireAdresse(processeur));
            }
        }
    }

    public int getAdresseEffective(Processeur processeur)
    {
        if (mode == ModeAdressage.Direct)
        {
            int dp = processeur.getRegistre(Registre.DP);
            return (dp << 8) + val;
        }
        if (mode == ModeAdressage.Indexe)
        {
            int deplacement = val;
            if (deplacementReg != null)
                deplacement = processeur.getRegistre(deplacementReg);

            return deplacement + processeur.getRegistre(modeIndexReg);
        }

        return val;
    }


    public int lireAdresse(Processeur processeur)
    {
        return switch (mode)
        {
            case Immediat -> val;
            case Direct -> getAdresseEffective(processeur);
            default -> getAdresseEffective(processeur); //Indexe
        };
    }

    public int lireValeur(Processeur processeur)
    {
        return switch (mode)
        {
            case Immediat -> val;
            case Direct -> {
                int adresse = getAdresseEffective(processeur);
                yield processeur.memoire().lire(adresse);
            }
            default -> {
                int adresse = getAdresseEffective(processeur);
                yield processeur.memoire().lire(adresse);
            } //Indexe
        };
    }
}