package main;

import factory_method.ReaderFactory;
import processor.FileReaderTemplate;

import java.io.IOException;
import java.nio.file.*;

public class Main {
    static final String LINK_IMPORT = "D:/ELCA_bootcamp/exercise_java_week1/company_management/src/import";
    static final String FILE_NAME = "company.csv";
    private static void watchFile(String link) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path folder = Paths.get(LINK_IMPORT);
            folder.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            System.out.println("Monitoring folder: " + LINK_IMPORT);
            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.OVERFLOW) continue;
                    Path modifiedFile = (Path) event.context();
                    if (modifiedFile.toString().equalsIgnoreCase(FILE_NAME)) {
                        System.out.println("=> FILE MODIFIED: " + modifiedFile);
                        calcFile(link);
                    }
                }
                key.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void calcFile(String link) {
        FileReaderTemplate processor = ReaderFactory.getFileReader(link);
        processor.calcTotalCapital(link, "CH");
        processor.listCompanies(link, "CH");
        System.out.println();
    }

    public static void main(String[] args) {
        String link = LINK_IMPORT + "/" + FILE_NAME;
        calcFile(link);
        watchFile(link);
    }
    // 21,PhuPhuoc Company,1/6/2023,0.777,CH,1

    /*
    * khởi tạo một WatchService bằng cách sử dụng phương thức newWatchService() từ FileSystems.getDefault().
    * WatchService theo dõi sự kiện thay đổi trong hệ thống tệp tin.
    *
    * tạo một Path từ đường dẫn LINK_IMPORT, đại diện cho thư mục mà ta muốn theo dõi sự thay đổi.
    * Paths.get() là một phương thức tạo object Path từ một chuỗi đường dẫn.
    *
    * đăng ký folder với watchService để theo dõi sự kiện ENTRY_MODIFY, tức là sự kiện khi một tệp tin trong thư mục bị sửa đổi.
    * bắt đầu theo dõi sự kiện trong thư mục.
    *
    * in ra thông báo "Monitoring folder: " LINK_IMPORT cho biết thư mục đang được theo dõi.
    *
    * một vòng lặp vô hạn while (true) để liên tục check event trong thư mục.
    *
    * Trong while, watchService.take() sẽ chờ đợi cho đến khi một event xảy ra trong folder và return một WatchKey để xử lý event đó.
    *
    * Vòng lặp For loop qua tất cả các sự kiện trong key.pollEvents() để check từng event.
    *     +   WatchEvent.Kind<?> kind = event.kind(); lấy ra loại sự kiện của event.
    *     +  check nếu kind là StandardWatchEventKinds.OVERFLOW,
    *               tức là có quá nhiều sự kiện xảy ra trong thư mục mà không thể xử lý kịp
    *               thì continue vòng lặp để check next event.
    *     +  Path modifiedFile = (Path) event.context(); lấy ra Path của tệp tin đã bị sửa đổi từ sự kiện event.context().
    *     +  if (modifiedFile.toString().equalsIgnoreCase(FILE_NAME)) compare tên của tệp tin bị sửa đổi với FILE_NAME.
    *               Nếu tên tệp tin trùng khớp, nghĩa là tệp tin mà ta watching đã được sửa đổi => thực hiện các action tiếp theo.
    *     +  System.out.println("=> FILE MODIFIED: " + modifiedFile); in ra thông báo cho biết tệp tin đã bị sửa đổi.
    *     +  calcFile(link); gọi phương thức calcFile với param link để thực hiện lại "feature #2" và "feature #3".
    *
    * Nếu không gọi key.reset(), WatchKey sẽ không nhận được các sự kiện mới và việc theo dõi sẽ dừng lại.
    * */
}