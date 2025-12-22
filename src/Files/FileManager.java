package Files;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;

public class FileManager
{
    public static String load()
    {
        JFileChooser chooser = new JFileChooser();
        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                File fichier = chooser.getSelectedFile();
                return Files.readString(fichier.toPath());
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        }

        return "";
    }

    public static void save(String text)
    {
        JFileChooser chooser = new JFileChooser();
        if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                File fichier = chooser.getSelectedFile();
                Files.writeString(fichier.toPath(), text);
            }
            catch(Exception e)
            {
                System.err.println(e.getMessage());
            }
        }
    }
}