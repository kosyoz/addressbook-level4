package seedu.address.logic.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.storage.VcfImport;
import seedu.address.storage.XmlAddressBookStorage;
import seedu.address.storage.XmlFileStorage;

//@@author freesoup
/**
 * Retrieves the location of the import file and passes the FileInputStream into
 * a new ImportCommand Object.
 */
public class ImportCommandParser implements Parser<ImportCommand> {
    public static final Pattern xmlFile = Pattern.compile("([^\\s] + (\\.xml$))");
    public static final Pattern vcfFile = Pattern.compile("([^\\s] + (\\.xml$))");

    @Override
    public ImportCommand parse(String userInput) throws ParseException {
        JButton open = new JButton();
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File("C:/"));
        fc.setDialogTitle("Select your vCard file");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (file.getName().endsWith(".xml")) {

                try {
                    ReadOnlyAddressBook importingBook = XmlFileStorage.loadDataFromSaveFile(file);
                    List<ReadOnlyPerson> importList = importingBook.getPersonList();
                    return new ImportCommand(importList);
                } catch (DataConversionException dce) {
                    throw new ParseException(ImportCommand.MESSAGE_FILE_CORRUPT);
                } catch (FileNotFoundException fnfe) {
                    throw new ParseException(ImportCommand.MESSAGE_FILE_NOTFOUND);
                }

            } else if (file.getName().endsWith(".vcf")) {

                    List<ReadOnlyPerson> importList = VcfImport.getPersonList();
                    return new ImportCommand(importList);

            } else {
                throw new ParseException(ImportCommand.MESSAGE_WRONG_FORMAT);
            }
        } else {
            throw new ParseException(ImportCommand.MESSAGE_NOFILECHOSEN);
        }
    }

}
