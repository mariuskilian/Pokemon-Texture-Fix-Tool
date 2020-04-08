package Default;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.ToolTipManager;

public class GUI {
    
    public static void popUpWindow() {
        final JFrame frame = new JFrame();
        frame.setTitle("PTFT by preylol");
        frame.setSize(600, 600);
        frame.setLocation(430, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        String title = "<html><header><h2 style=\"text-align:center\">Thanks for using Pokemon Texture Fix Tool</h2>"
            + "<p style=\"text-align:center\">Created by Preylol</p><br><br></header></html>";
        JLabel titleLabel = new JLabel(title);
        titlePanel.add(titleLabel);
        titlePanel.setVisible(true);

        JPanel descriptionPanel = new JPanel();
        String description = "READ: This tool was created to fix Pokemon Textures. When extracting"
            + " Pokemon models and textures from a 3DS or Switch, a lot of the textures are"
            + " halved to save space. This tool will automatically double the width of each"
            + " texture, and then mirror the contents of the left side to the right side."
            + " The new image will be saved with the Prefix \"Fixed_\", leaving the original"
            + " image intact, becuase some Models\'"
            + " texture files are not halved.\n\nHover the texts below for more information on"
            + " that specific field.\n\nMAKE SURE TO RUN ONLY FROM INSIDE THE FOLDER WITH THE TEXTURES,"
            + " OR ALL YOUR IMAGES WILL SOON HAVE \"Fixed_\" VERSIONS OF THEM ;)";
        JTextArea descriptionText = new JTextArea(15, 30);
        descriptionText.setText(description);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setLineWrap(true);
        descriptionText.setOpaque(false);
        descriptionText.setEditable(false);
        descriptionText.setFocusable(false);
        descriptionPanel.add(descriptionText);
        descriptionPanel.setVisible(true);

        JPanel optionsPanel = new JPanel(new GridLayout(9, 1));

        JPanel prefixPanel = new JPanel();
        JLabel prefixLabel = new JLabel("Only affect files with the following prefix:");
        prefixLabel.setToolTipText("Leave empty to affect all images. As an example: A prefix most, but not all texture files have is \"pm0\"");
        JTextField prefixText = new JTextField(Main.prefix);
        prefixText.setPreferredSize(new Dimension(100, 25));
        prefixPanel.add(prefixLabel);
        prefixPanel.add(prefixText);
        prefixPanel.setVisible(true);

        JPanel endingPanel = new JPanel();
        JLabel endingLabel = new JLabel("Only affect files with the following ending:");
        endingLabel.setToolTipText("Leave empty to affect all images. As an example: Common file endings are \"png\" or \"tga\"");
        JTextField endingText = new JTextField(Main.ending);
        endingText.setPreferredSize(new Dimension(100, 25));
        endingPanel.add(endingLabel);
        endingPanel.add(endingText);
        endingPanel.setVisible(true);

        JPanel locationPanel = new JPanel();
        JLabel locationLabel = new JLabel("Location of \"Fixed\" in file name:");
        locationLabel.setToolTipText("<html>Prefix - \"Fixed_[filename].[fileending]\"<br>Postfix - \"[filename]_Fixed.[fileending]\""
            + "<br><br>Example:<br>Prefix - Fixed_001_Bulbasaur_Standard_Body_Albedo.png<br>Postfix - 001_Bulbasaur_Standard_Body_Albedo_Fixed.png</html>");
        JComboBox<String> locationMenu = new JComboBox<>(Main.locationFixedOptions);
        locationMenu.setSelectedIndex(Main.locationFixed);
        locationPanel.add(locationLabel);
        locationPanel.add(locationMenu);
        locationPanel.setVisible(true);

        JPanel mergedPanel = new JPanel();
        JCheckBox mergedBox = new JCheckBox("Create Merged Eye Texture", Main.createEyeMerged);
        mergedBox.setToolTipText("<html>Uses the eye, iris and eye alpha/normal textures to create a Eye_Merged texture (Both a normal and a \"fixed\" version)<br>"
            + "This will create an Eye_Merged file for each pokemons textures it finds, that does not yet have an Eye_Merged file</html>");
        mergedPanel.add(mergedBox);
        mergedPanel.setVisible(true);

        JPanel subfolderPanel = new JPanel();
        JCheckBox subfolderBox = new JCheckBox("Affect subfolders", Main.affectSubfolders);
        subfolderBox.setToolTipText("<html>Off - only affect images in the folder the program is run from<br><br>"
            + "On - affect all subfolders of that folder, and all subfolders of the subfolders too</html>");
        subfolderPanel.add(subfolderBox);
        subfolderPanel.setVisible(true);

        JPanel formatPanel = new JPanel();
        JLabel formatLabel = new JLabel("Format file names:");
        formatLabel.setToolTipText("<html>No - Keeps original file names, and adds \"Fixed_\" to the front of the fixed ones<br><br>"
            + "Try - Attempts to rename the files using a built in Rename function. This function might not work on some systems,<br>keeping the old name."
            + " The fixed files will have the \"Fixed_\" prefix and be renamed, even if the original ones aren't<br><br>"
            + "Force - Will create a duplicate file of the original file, which will be correctly renamed, as well as a fixed file,<br>also renamed and with a \"Fixed_\" prefix."
            + " An option will appear to delete the original files<br><br><br><br>The renaming process tries to create more readable file names. Cannot guarantee perfect file names.<br>"
            + " For example, \"pm0004\" would be replaced with \"004_Charmander\". However, I do not have a list of all Pokemon names.<br>If your Pokemon is not on the list,"
            + " it would instead simply rename \"pm0004\" to \"004\". Other things,<br>like Occlusion maps being given the name \"_Occlusion\", etc. will try to be applied.</html>");
        JComboBox<String> formatMenu = new JComboBox<>(Main.formatFileNameOptions);
        formatMenu.setSelectedIndex(Main.formatFileName);
        formatPanel.add(formatLabel);
        formatPanel.add(formatMenu);
        formatPanel.setVisible(true);

        JPanel deleteOldPanel = new JPanel();
        JCheckBox deleteOldBox = new JCheckBox("Delete old Files (hover for more info):", Main.deleteOldFiles);
        deleteOldBox.setToolTipText("<html>Will delete the original files after creating the duplicate file with the<br>new name. Use at own risk. Hover \"Format file names:\" for more info</html>");
        deleteOldPanel.add(deleteOldBox);
        deleteOldPanel.setVisible(false);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton();
        confirmButton.setText("Run PTFT!");
        buttonPanel.add(confirmButton);
        buttonPanel.setVisible(true);

        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        optionsPanel.add(prefixPanel);
        optionsPanel.add(endingPanel);
        optionsPanel.add(locationPanel);
        optionsPanel.add(mergedPanel);
        optionsPanel.add(subfolderPanel);
        optionsPanel.add(formatPanel);
        optionsPanel.add(deleteOldPanel);
        optionsPanel.add(buttonPanel);
        optionsPanel.setVisible(true);

        JPanel content = new JPanel(new GridLayout(2, 1));
        content.add(descriptionPanel);
        content.add(optionsPanel);
        content.setVisible(true);

        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(content, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        formatMenu.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                if (formatMenu.getSelectedIndex() == 2) {
                    deleteOldPanel.setVisible(true);
                } else {
                    deleteOldBox.setSelected(false);
                    deleteOldPanel.setVisible(false);
                }
            }
        });
        
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String message = "This will affect all ." + Main.ending
                    + "files in this folder";
                message += (Main.affectSubfolders) ? " and all sub-folders" : "";
                message += ". Are you sure you want to continue?";
                if (JOptionPane.showConfirmDialog(frame, message, "Confirm", JOptionPane.OK_CANCEL_OPTION) == 0) {
                    Main.prefix = prefixText.getText();
                    Main.ending = endingText.getText();
                    Main.createEyeMerged = mergedBox.isSelected();
                    Main.affectSubfolders = subfolderBox.isSelected();
                    Main.formatFileName = formatMenu.getSelectedIndex();
                    Main.deleteOldFiles = Main.formatFileName == 2 && deleteOldBox.isSelected();
                    PTFT.runMainProgram();
                    frame.dispose();
                };
                
            }
        });
    }
}