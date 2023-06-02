package factory_method;

import processor.CSVFileReader;
import processor.FileReaderTemplate;
import processor.XLSXFileReader;
import processor.XMLFileReader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ReaderFactory {

    private ReaderFactory() {
    }

    public static FileReaderTemplate getFileReader(String link) {
        Path path = Paths.get(link);
        String filename = path.getFileName().toString();
        if (filename.endsWith(".csv")) {
            return new CSVFileReader();
        } else if (filename.endsWith(".xml")) {
            return new XMLFileReader();
        } else if (filename.endsWith(".xlsx")) {
            return new XLSXFileReader();
        } else {
            throw new IllegalArgumentException("This file is unsupported!");
        }
    }
}