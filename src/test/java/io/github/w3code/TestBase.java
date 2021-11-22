package io.github.w3code;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;

import java.nio.charset.Charset;

public class TestBase {
    public static String convertToCP866(String text) {
        return new String(text.getBytes(), Charset.forName("CP866"));
    }

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1920x1080";
    }
}
