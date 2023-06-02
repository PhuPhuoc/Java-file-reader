package processor;

import dto.Company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CSVFileReader extends FileReaderTemplate {
    private static final String csvSplit = ",";

    @Override
    public void calcTotalCapital(String filename, String country) {
        String line;
        double totalCap = 0;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filename));
            br.readLine(); // skip rows 1
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplit);
                if (data.length == 6 && data[4].equalsIgnoreCase(country) && data[5].equalsIgnoreCase("1")) {
                    double capital = Double.parseDouble(data[3]);
                    totalCap += capital;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println(" >> Total capital of headquarters located in 'CH': " + totalCap + " << ");
        }
    }

    @Override
    public void listCompanies(String filename, String country) {
        String line;
        BufferedReader br;
        List<Company> companyList = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(filename));
            br.readLine(); // skip first line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplit);
                if (data.length > 4 && data[4].equalsIgnoreCase(country)) {
                    String name = data[1];
                    double capital = Double.parseDouble(data[3]);
                    Company com = new Company(name, capital);
                    companyList.add(com);
                }
            }
            // sort the list of company descending by capital.
            companyList.sort(new Comparator<Company>() {
                @Override
                public int compare(Company o1, Company o2) {
                    return o1.getCapital() > o2.getCapital() ? -1 : 1;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            int i = 0;
            System.out.println(" >>  Name of companies that the country is in “CH” << ");
            for (Company com : companyList) {
                System.out.print(++i + "/ " + com.getName() + "    ");
            }
        }
    }
}