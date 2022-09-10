package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import org.junit.jupiter.api.Test;

public class WebTest {
    String expectedMovieName = "True Detective";
    @BeforeAll
    static void browserParams() {
        Configuration.timeout = 10000;
        Configuration.browserSize = "1920x1080";
    }

    @ValueSource(strings = {"Dete", "true"})
    @ParameterizedTest(name = "Результаты поиска по {0} содержат сериал True Detective")
    void imdbSearchTest(String testData) {
        open("https://www.imdb.com/");
        $("#suggestion-search").setValue(testData).hover();
        //$(".findList").shouldHave(text(" (2014) (TV Series) "));
        $("#react-autowhatever-1--item-0").shouldHave(text(expectedMovieName));
    }

    @CsvSource(value = {
            "pivot | Друзья",
            "I'm gonna make him an offer he can't refuse | Крестный отец"},
            delimiter = '|')
    @ParameterizedTest(name = "Результаты поиска по цитате {0} содержит фильм {1}")
    void commonComplexSearchTest(String testData, String expectedResult) {
        open("https://www.imdb.com/");
        $(".sc-3c88d23-0").click();
        $("#iconContext-find-in-page").click();
        $("#type").selectOptionContainingText("Quotes");
        $("#query").setValue(testData).pressEnter();
        $(".lister-list").shouldHave(text(expectedResult));
    }

    static Stream<Arguments> dataProviderTest() {
        return Stream.of(
                Arguments.of($("#language-option-it-IT"), List.of("Film", "Serie TV", "Guarda", "Premi ed eventi", "Celebrità", "Comunità")),
                Arguments.of($("#language-option-en-US"), List.of("Movies", "TV Shows", "Watch", "Awards & Events", "Celebs", "Community"))
        );
    }
    @MethodSource("dataProviderTest")
    @ParameterizedTest(name = "Выбранной локале соответствуют разделы {1}")
    void imdbMenuContentTest(SelenideElement lang, List<String> expectedButtons) {
        open("https://www.imdb.com/");
        $("#imdbHeader > div.ipc-page-content-container.ipc-page-content-container--center.navbar__inner > div.sc-dQppl.sc-fKFyDc.cQZIoF.nwOmR.navbar__flyout--breakpoint-m").click();
        lang.click();
        $(".ipc-button__text").click();
       // lang.click();
        $$(".navlinkcat__item").shouldHave(CollectionCondition.texts(expectedButtons));
    }

}