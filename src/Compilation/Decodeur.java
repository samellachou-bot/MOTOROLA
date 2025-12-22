package Compilation;

import Components.Memoire;
import Components.Types.Registre;
import Execution.Instructions.Instruction;
import Execution.Instructions.ModeAdressage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decodeur
{
    private final Memoire memoire;
    private final List<String> instructions = new ArrayList<>();
    private final Map<String, Integer> opCodeTable = new HashMap<>();

    public Decodeur(Memoire memoire)
    {
        this.memoire = memoire;
        initializerOpCodeTable();
    }

    public void initializerOpCodeTable()
    {
        // ABX (inherent)
        opCodeTable.put("ABX_INH", 0x3A);

        // ADCA
        opCodeTable.put("ADCA_IMM", 0x89);
        opCodeTable.put("ADCA_DIR", 0x99);
        opCodeTable.put("ADCA_IDX", 0xA9);
        opCodeTable.put("ADCA_EXT", 0xB9);

        // ADCB
        opCodeTable.put("ADCB_IMM", 0xC9);
        opCodeTable.put("ADCB_DIR", 0xD9);
        opCodeTable.put("ADCB_IDX", 0xE9);
        opCodeTable.put("ADCB_EXT", 0xF9);

        // ADDA
        opCodeTable.put("ADDA_IMM", 0x8B);
        opCodeTable.put("ADDA_DIR", 0x9B);
        opCodeTable.put("ADDA_IDX", 0xAB);
        opCodeTable.put("ADDA_EXT", 0xBB);

        // ADDB
        opCodeTable.put("ADDB_IMM", 0xCB);
        opCodeTable.put("ADDB_DIR", 0xDB);
        opCodeTable.put("ADDB_IDX", 0xEB);
        opCodeTable.put("ADDB_EXT", 0xFB);

        // ADDD
        opCodeTable.put("ADDD_IMM", 0xC3);
        opCodeTable.put("ADDD_DIR", 0xD3);
        opCodeTable.put("ADDD_IDX", 0xE3);
        opCodeTable.put("ADDD_EXT", 0xF3);

        // ASLA
        opCodeTable.put("ASLA_INH", 0x48);

        // ASLB
        opCodeTable.put("ASLB_INH", 0x58);

        // ASL (mode direct/indexé/étendu)
        opCodeTable.put("ASL_DIR", 0x08);
        opCodeTable.put("ASL_IDX", 0x68);
        opCodeTable.put("ASL_EXT", 0x78);

        // ANDA
        opCodeTable.put("ANDA_IMM", 0x84);
        opCodeTable.put("ANDA_DIR", 0x94);
        opCodeTable.put("ANDA_IDX", 0xA4);
        opCodeTable.put("ANDA_EXT", 0xB4);

        // ANDB
        opCodeTable.put("ANDB_IMM", 0xC4);
        opCodeTable.put("ANDB_DIR", 0xD4);
        opCodeTable.put("ANDB_IDX", 0xE4);
        opCodeTable.put("ANDB_EXT", 0xF4);

        // ANDCC
        opCodeTable.put("ANDCC_DIR", 0x1C);

        // ASRA
        opCodeTable.put("ASRA_INH", 0x47);

        // ASRB
        opCodeTable.put("ASRB_INH", 0x57);

        // ASR
        opCodeTable.put("ASR_DIR", 0x07);
        opCodeTable.put("ASR_IDX", 0x67);
        opCodeTable.put("ASR_EXT", 0x77);

        // BITA
        opCodeTable.put("BITA_IMM", 0x85);
        opCodeTable.put("BITA_DIR", 0x95);
        opCodeTable.put("BITA_IDX", 0xA5);
        opCodeTable.put("BITA_EXT", 0xB5);

        // BITB
        opCodeTable.put("BITB_IMM", 0xC5);
        opCodeTable.put("BITB_DIR", 0xD5);
        opCodeTable.put("BITB_IDX", 0xE5);
        opCodeTable.put("BITB_EXT", 0xF5);

        // CLRA
        opCodeTable.put("CLRA_INH", 0x4F);

        // CLRB
        opCodeTable.put("CLRB_INH", 0x5F);

        // CLR
        opCodeTable.put("CLR_DIR", 0x0F);
        opCodeTable.put("CLR_IDX", 0x6F);
        opCodeTable.put("CLR_EXT", 0x7F);

        // CMPA
        opCodeTable.put("CMPA_IMM", 0x81);
        opCodeTable.put("CMPA_DIR", 0x91);
        opCodeTable.put("CMPA_IDX", 0xA1);
        opCodeTable.put("CMPA_EXT", 0xB1);

        // CMPD
        opCodeTable.put("CMPD_IMM", 0x1083);
        opCodeTable.put("CMPD_DIR", 0x1093);
        opCodeTable.put("CMPD_IDX", 0x10A3);
        opCodeTable.put("CMPD_EXT", 0x10B3);

        // CMPX
        opCodeTable.put("CMPX_IMM", 0x8C);
        opCodeTable.put("CMPX_DIR", 0x9C);
        opCodeTable.put("CMPX_IDX", 0xAC);
        opCodeTable.put("CMPX_EXT", 0xBC);

        // COMA
        opCodeTable.put("COMA_INH", 0x43);

        // COMB
        opCodeTable.put("COMB_INH", 0x53);

        // COM
        opCodeTable.put("COM_DIR", 0x03);
        opCodeTable.put("COM_IDX", 0x63);
        opCodeTable.put("COM_EXT", 0x73);

        // DECA
        opCodeTable.put("DECA_INH", 0x4A);

        // DEC
        opCodeTable.put("DEC_DIR", 0x0A);
        opCodeTable.put("DEC_IDX", 0x6A);
        opCodeTable.put("DEC_EXT", 0x7A);

        // EORA
        opCodeTable.put("EORA_IMM", 0x88);
        opCodeTable.put("EORA_DIR", 0x98);
        opCodeTable.put("EORA_IDX", 0xA8);
        opCodeTable.put("EORA_EXT", 0xB8);

        // EORB
        opCodeTable.put("EORB_IMM", 0xC8);
        opCodeTable.put("EORB_DIR", 0xD8);
        opCodeTable.put("EORB_IDX", 0xE8);
        opCodeTable.put("EORB_EXT", 0xF8);

        // EXG
        opCodeTable.put("EXG_INH", 0x1E);

        // INCA
        opCodeTable.put("INCA_INH", 0x4C);

        // INCB
        opCodeTable.put("INCB_INH", 0x5C);

        // INC
        opCodeTable.put("INC_DIR", 0x0C);
        opCodeTable.put("INC_IDX", 0x6C);
        opCodeTable.put("INC_EXT", 0x7C);

        // LDA
        opCodeTable.put("LDA_IMM", 0x86);
        opCodeTable.put("LDA_DIR", 0x96);
        opCodeTable.put("LDA_IDX", 0xA6);
        opCodeTable.put("LDA_EXT", 0xB6);

        // LDB
        opCodeTable.put("LDB_IMM", 0xC6);
        opCodeTable.put("LDB_DIR", 0xD6);
        opCodeTable.put("LDB_IDX", 0xE6);
        opCodeTable.put("LDB_EXT", 0xF6);

        // LDD
        opCodeTable.put("LDD_IMM", 0xCC);
        opCodeTable.put("LDD_DIR", 0xDC);
        opCodeTable.put("LDD_IDX", 0xEC);
        opCodeTable.put("LDD_EXT", 0xFC);

        // LDY
        opCodeTable.put("LDY_IMM", 0x108E);
        opCodeTable.put("LDY_DIR", 0x109E);
        opCodeTable.put("LDY_IDX", 0x10AE);
        opCodeTable.put("LDY_EXT", 0x10BE);
        
        // LDX
        opCodeTable.put("LDX_IMM", 0x108E);
        opCodeTable.put("LDX_DIR", 0x109E);
        opCodeTable.put("LDX_IDX", 0x10AE);
        opCodeTable.put("LDX_EXT", 0x10BE);
        
     // LDS
        opCodeTable.put("LDS_IMM", 0x108E);
        opCodeTable.put("LDS_DIR", 0x109E);
        opCodeTable.put("LDS_IDX", 0x10AE);
        opCodeTable.put("LDS_EXT", 0x10BE);
        
     // LDU
        opCodeTable.put("LDU_IMM", 0x108E);
        opCodeTable.put("LDU_DIR", 0x109E);
        opCodeTable.put("LDU_IDX", 0x10AE);
        opCodeTable.put("LDU_EXT", 0x10BE);

        // LEAX
        opCodeTable.put("LEAX_IDX", 0x30);

        // LSLA
        opCodeTable.put("LSLA_INH", 0x48);

        // LSLB
        opCodeTable.put("LSLB_INH", 0x58);

        // LSL
        opCodeTable.put("LSL_DIR", 0x08);
        opCodeTable.put("LSL_IDX", 0x68);
        opCodeTable.put("LSL_EXT", 0x78);

        // LSRA
        opCodeTable.put("LSRA_INH", 0x44);

        // LSRB
        opCodeTable.put("LSRB_INH", 0x54);

        // LSR
        opCodeTable.put("LSR_DIR", 0x04);
        opCodeTable.put("LSR_IDX", 0x64);
        opCodeTable.put("LSR_EXT", 0x74);

        // MUL
        opCodeTable.put("MUL_INH", 0x3D);

        // NEGA
        opCodeTable.put("NEGA_INH", 0x40);

        // NEGB
        opCodeTable.put("NEGB_INH", 0x50);

        // NEG
        opCodeTable.put("NEG_DIR", 0x00);
        opCodeTable.put("NEG_IDX", 0x60);
        opCodeTable.put("NEG_EXT", 0x70);

        // NOP
        opCodeTable.put("NOP_INH", 0x12);

        // ORA
        opCodeTable.put("ORA_IMM", 0x8A);
        opCodeTable.put("ORA_DIR", 0x9A);
        opCodeTable.put("ORA_IDX", 0xAA);
        opCodeTable.put("ORA_EXT", 0xBA);

        // ORB
        opCodeTable.put("ORB_IMM", 0xCA);
        opCodeTable.put("ORB_DIR", 0xDA);
        opCodeTable.put("ORB_IDX", 0xEA);
        opCodeTable.put("ORB_EXT", 0xFA);

        // ORCC
        opCodeTable.put("ORCC_IMM", 0x1A);

        // SBCA
        opCodeTable.put("SBCA_IMM", 0x82);
        opCodeTable.put("SBCA_DIR", 0x92);
        opCodeTable.put("SBCA_IDX", 0xA2);
        opCodeTable.put("SBCA_EXT", 0xB2);

        // SBCB
        opCodeTable.put("SBCB_IMM", 0xC2);
        opCodeTable.put("SBCB_DIR", 0xD2);
        opCodeTable.put("SBCB_IDX", 0xE2);
        opCodeTable.put("SBCB_EXT", 0xF2);

        // STA
        opCodeTable.put("STA_DIR", 0x97);
        opCodeTable.put("STA_IDX", 0xA7);
        opCodeTable.put("STA_EXT", 0xB7);

        // STB
        opCodeTable.put("STB_DIR", 0xD7);
        opCodeTable.put("STB_IDX", 0xE7);
        opCodeTable.put("STB_EXT", 0xF7);

        // STS
        opCodeTable.put("STS_DIR", 0x10DF);
        opCodeTable.put("STS_IDX", 0x10EF);
        opCodeTable.put("STS_EXT", 0x10FF);

        // STU
        opCodeTable.put("STU_DIR", 0xDF);
        opCodeTable.put("STU_IDX", 0xEF);
        opCodeTable.put("STU_EXT", 0xFF);

        // SUBA
        opCodeTable.put("SUBA_IMM", 0x80);
        opCodeTable.put("SUBA_DIR", 0x90);
        opCodeTable.put("SUBA_IDX", 0xA0);
        opCodeTable.put("SUBA_EXT", 0xB0);

        // SUBB
        opCodeTable.put("SUBB_IMM", 0xC0);
        opCodeTable.put("SUBB_DIR", 0xD0);
        opCodeTable.put("SUBB_IDX", 0xE0);
        opCodeTable.put("SUBB_EXT", 0xF0);

        // SUBD
        opCodeTable.put("SUBD_IMM", 0x83);
        opCodeTable.put("SUBD_DIR", 0x93);
        opCodeTable.put("SUBD_IDX", 0xA3);
        opCodeTable.put("SUBD_EXT", 0xB3);

        // SWI
        opCodeTable.put("SWI_INH", 0x3F);

        // SWI3
        opCodeTable.put("SWI3_INH", 0x113F);

        // TFR
        opCodeTable.put("TFR_INH", 0x1F);

        // TSTA
        opCodeTable.put("TSTA_INH", 0x4D);

        // TST
        opCodeTable.put("TST_DIR", 0x0D);
        opCodeTable.put("TST_IDX", 0x6D);
        opCodeTable.put("TST_EXT", 0x7D);

        instructions.clear();
        for (String instrMode : opCodeTable.keySet())
        {
            String mnemonic = instrMode.replaceAll("_.*", "");
            if (!instructions.contains(mnemonic))
            {
                instructions.add(mnemonic);
            }
        }
    }

    public int getOpcode(String key)
    {
        return opCodeTable.getOrDefault(key.toUpperCase(), -1);
    }

    public HashMap<Integer, Instruction> decoder(String code)
    {
        int pc = 0;
        HashMap<Integer, Instruction> instructions = new HashMap<>();

        boolean erreur = false;
        if (!code.trim().toUpperCase().endsWith("\nEND"))
        {
            System.out.println("Le code doit terminée avec END");
            return instructions;
        }
        try
        {
            String[] lignes = code.split("\\n");
            for (int ligneIndex = 0; ligneIndex < lignes.length; ligneIndex++)
            {
                String ligne = lignes[ligneIndex].replaceAll(";.*", "").trim();
                if(ligne.isBlank() || ligne.equalsIgnoreCase("END"))
                    continue;

                Instruction instruction = decoderLigne(ligne, pc, ligneIndex + 1);
                if (instruction == null)
                {
                    erreur = true;
                    continue;
                }

                instructions.put(pc, instruction);
                pc += instruction.size();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if (erreur)
            instructions.clear();
        else
            System.out.println("Compilation terminée sans erreurs");
        return instructions;
    }

    private Instruction decoderLigne(String ligne, int pc, int ligneIndice)
    {
        String[] tokens = ligne.split("\\s+");
        String mnemonic = tokens[0].toUpperCase();
        if (!instructions.contains(mnemonic))
        {
            System.out.println("A ligne: " + ligneIndice + ": Instruction " + mnemonic + " inconnue");
            return null;
        }

        if (tokens.length == 1)
            return null;

        int val = 0;
        Registre modeIndexReg = Registre.X,
                 deplacementRegistre = null;

        ModeAdressage mode = ModeAdressage.Immediat;
        String operand = tokens[1].toUpperCase();

        if(operand.startsWith("#"))
        {
            operand = operand.substring(1);
            if (!operand.startsWith("$"))
            {
                System.out.println("A ligne: " + ligneIndice + ": l'opérand doit être commencée par $");
                return null;
            }

            operand = operand.replace("$","");
            val = Integer.parseInt(operand, 16);
        }
        else if(operand.contains(","))
        {
            mode = ModeAdressage.Indexe;
            String[] parts = operand.split(",");
            deplacementRegistre = switch (parts[0].toUpperCase())
            {
                case "A" -> Registre.A;
                case "B" -> Registre.B;
                case "D" -> Registre.D;
                default -> null;
            };

            String offsetStr = parts[0].replace("$","").toUpperCase();
            if (deplacementRegistre == null && !parts[0].isEmpty() && !isInt(offsetStr))
            {
                System.out.println("A ligne: " + ligneIndice + ": Deplacement/Registre " +  parts[0] + " est invalide");
                return null;
            }

            if (isInt(offsetStr))
            {
                val = Integer.parseInt(offsetStr, 16);
            }
            
            if(parts[0].isEmpty())
            {
            	val = 0 ;
            }

            
            
            modeIndexReg = switch (parts[1].toUpperCase())
            {
                case "Y" -> Registre.Y;
                case "U" -> Registre.U;
                case "S" -> Registre.S;
                case "PC" -> Registre.PC;
                case "X" -> Registre.X;
                default -> null;
            };

            if (modeIndexReg == null)
            {
                System.out.println("A ligne: " + ligneIndice + ": Index Register " + parts[1] + " est non valide");
                return null;
            }
        }
        else
        {
            mode = ModeAdressage.Direct;

            String valStr = operand.replace("$","");
            if (!isInt(valStr))
            {
                System.out.println("A ligne: " + ligneIndice + ": Operand " + operand + " est non valide");
                return null;
            }

            if (!operand.startsWith("$"))
            {
                System.out.println("A ligne: " + ligneIndice + ": l'opérand doit être commencée par $");
                return null;
            }

            val = Integer.parseInt(valStr, 16);
            val &= 0xFF;
        }

        // Construire la clé pour récupérer l'opcode exact
        String key = mnemonic;
        switch(mode)
        {
            case Immediat:
                key += "_IMM";
                break;
            case Direct:
                key += "_DIR";
                break;
            case Indexe:
                key += "_IDX";
                break;
        }

        int opcode = getOpcode(key);
        if(opcode == -1)
        {
            System.out.println("A ligne: " + ligneIndice + "Instruction inconnue : " + mnemonic);
            return null;
        }

        memoire.ecrire(pc, opcode);
        int size = opcode > 0xFF ? 2 : 1;
        pc += size;

        if (mode == ModeAdressage.Indexe)
        {
            memoire.ecrire(pc, calculerPostByte(deplacementRegistre, modeIndexReg, val, ",X"));
            pc++;
        }

        if (mode != ModeAdressage.Indexe)
        {
            memoire.ecrire(pc, val);
            size += val > 0xFF ? 2 : 1;
        }

        return new Instruction(mnemonic, mode, size, val, null, modeIndexReg);
    }

    private int calculerPostByte(Registre deplacementRegistre, Registre modeIndexReg, int offset,String operandStr)
    {
    	int postByte = 0x80;
        int regCode = switch (modeIndexReg) {
            case X -> 0x00;
            case Y -> 0x01;
            case U -> 0x02;
            default -> 0x03;
        };

        postByte |= regCode << 5;
        if (deplacementRegistre != null)
        {
            postByte |= switch(deplacementRegistre)
            {
                case A -> 0b00110;
                case B -> 0b00101;
                default -> 0b01011;
            };

            return (byte)postByte;
        }
        if (operandStr.contains("+") || operandStr.contains("-"))
        {
            boolean isPositive = operandStr.contains("+");
            int opCount = (int)operandStr.chars().filter(c -> c == '+' || c == '-').count();

            if (opCount == 1)
            {
                postByte |= isPositive ? 0x0 : 0x2;
            }
            else if (opCount == 2)
            {
                postByte |= isPositive ? 0x1 : 0x3;
            }

            return (byte)postByte;
        }

      
        if (offset == 0)
        {
            postByte |= 0b00100;
        }
        else if (offset < 0b10_0000) // 5 bit offset
        {
            postByte |= offset;
        }
        else if (offset < 0x100) //8 bit offset
        {
            if (modeIndexReg == Registre.PC)
                postByte |= 0b01100;
            else
                postByte |= 0b01000;
        }
        else //16 bit offset
        {
            if (modeIndexReg == Registre.PC)
                postByte |= 0b01101;
            else
                postByte |= 0b01001;
        }

        return (byte)postByte;
    }


    private boolean isInt(String s)
    {
        try
        {
            Integer.parseInt(s, 16);
            return true;
        }
        catch (NumberFormatException ignored)
        {

        }

        return false;
    }
}
