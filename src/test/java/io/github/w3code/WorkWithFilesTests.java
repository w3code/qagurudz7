package io.github.w3code;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.monitorjbl.xlsx.StreamingReader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static io.github.w3code.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkWithFilesTests extends TestBase {

    @Disabled
    @Test
    @DisplayName("Download and check CSV file")
    void parseCsvFileTest() throws IOException, CsvException {
        open(CSV_FILE_DOWNLOAD_LINK);
        InputStream is = new FileInputStream($(withText("скачать файл.csv")).download());
        Reader reader = new InputStreamReader(is);
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> strings = csvReader.readAll();
        assertEquals(7, strings.size());
    }

    @Disabled
    @Test
    @DisplayName("Download and check XLS file")
    void downloadAndCheckXLSFileTest() throws FileNotFoundException {
        open(XLS_FILE_DOWNLOAD_LINK);
        File file = $(withText("скачать файл.xls")).download();
        XLS parsedXls = new XLS(file);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(6)
                .getCell(2)
                .getStringCellValue()
                .contains("Закупки");
        assertTrue(checkPassed);
    }

    @Disabled
    @Test
    @DisplayName("Download and check XLSX file")
    void downloadAndCheckXLSXFileTest() throws IOException {
        open(XLSX_FILE_DOWNLOAD_LINK);
        InputStream is = new FileInputStream($(withText("скачать файл.xlsx")).download());
        Workbook workbook = StreamingReader.builder().open(is);
        boolean checkPassed = workbook.getSheetAt(0).getSheetName().contains("TemplateImportOU");
        assertTrue(checkPassed);
    }

    @Disabled
    @Test
    @DisplayName("Download and check PDF file")
    void downloadAndCheckPDFFileTest() throws IOException {
        open(PDF_FILE_DOWNLOAD_LINK);
        File pdf = $x("//a[contains(@href,'.pdf')]").download();
        PDF parsedPdf = new PDF(pdf);
        assertEquals(66, parsedPdf.numberOfPages);
    }

    @Test
    @DisplayName("Download and check ZIP file")
    void downloadAndCheckZipFileTest() throws IOException {
        open(ZIP_FILE_DOWNLOAD_LINK);
        InputStream is = new FileInputStream($(withText("Скачать всю документацию")).parent().download());
        ZipInputStream zis = new ZipInputStream(is, Charset.forName("windows-1251"));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            System.out.println(convertToCP866(entry.getName()));
        }
    }

    @Disabled
    @Test
    @DisplayName("Upload to image hosting")
    void uploadToImageHostingFromClasspathTest() {
        open(UPLOAD_IMAGE_LINK);
        $(".standard_upload").uploadFromClasspath("test.jpg");
        $("#standard-upload").click();
        $(".code-block-title").shouldHave(text("Страница с изображением (короткая ссылка)"));
    }
}
