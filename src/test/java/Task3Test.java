import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Домашнее задание №3
 * Тест Яндекс Маркета
 * Александр Кремлёв
 * 2.0
 */

public class Task3Test extends BaseTest {

    private String samsungId;
    private String xiaomiId;

    @Test
    public void yandexTest() {
        logger.info("Run Test for Yandex Market");

        //driver.manage().window().maximize();

        //Открываем главную страницу маркета
        openPage(cfg.urlYandex());

        //Соглашаемся со всем
        confirmPopUpIfExists(By.xpath(cfg.alert()));

        //Переходим в раздел Электроника
        clickByElement(By.cssSelector(cfg.electronicChapter()));

        //Переходим в раздел смартфоны
        clickByElement(By.cssSelector(cfg.phoneChapter()));

        //Соглашаемся со всем
        confirmPopUpIfExists(By.xpath(cfg.alert()));

        //Отмечаем чекбоксы Самсунг и Сяоми
        clickByElement(By.cssSelector(cfg.samsungBox()));
        clickByElement(By.cssSelector(cfg.xiaomiBox()));

        //Сортируем по цене
        clickByElement(By.cssSelector(cfg.sortByPrice()));

        //ждём пока прогрузится новый список с элементами
        new WebDriverWait(driver, 3).until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(cfg.firstSamsung()))));

        //Ищем первый Самсунг
        WebElement firstSamsung = findElement(By.cssSelector(cfg.firstSamsung()));

        //запоминаем идентификатор
        samsungId = parseId(firstSamsung);

        //добавляем к сравнению
        String idSelector = String.format("article[data-zone-data*='%s'] div[aria-label*='сравнению']", samsungId);
        clickByElement(By.cssSelector(idSelector));
        WebElement addedSamsung = findElement(By.xpath(cfg.add()));

        //Проверяем что телефон добавлен
        Assert.assertTrue(addedSamsung.getText().contains("Samsung"));
        logger.info("Samsung added " + addedSamsung.getText());

        //Ищем первый Сяоми
        WebElement firstXiaomi = findElement(By.cssSelector(cfg.firstXiaomi()));

        //запоминаем идентификатор
        xiaomiId = parseId(firstXiaomi);

        //добавляем к сравнению
        idSelector = String.format("article[data-zone-data*='%s'] div[aria-label*='сравнению']", xiaomiId);
        clickByElement(By.cssSelector(idSelector));
        WebElement addedXiaomi = findElement(By.xpath(cfg.add()));

        //Проверяем что телефон добавлен
        Assert.assertTrue(addedXiaomi.getText().contains("Xiaomi"));
        logger.info("Xiaomi added " + addedXiaomi.getText());

        //Сравниваем
        driver.get(cfg.compare());

        //Проверяем по идентификатору, что выбранные ранее телефоны добавлены к сравнению
        WebElement samsung = findElement(By.cssSelector("a[href*='/" + samsungId +"']"));
        Assert.assertTrue(samsung.isDisplayed());
        logger.info("Samsung find " + samsung.getText());

        WebElement xiaomi = findElement(By.cssSelector("a[href*='/" + xiaomiId +"']"));
        Assert.assertTrue(xiaomi.isDisplayed());
        logger.info("Xiaomi find " + xiaomi.getText());

        //Тест пройден
        logger.info("Test is passed");
    }

    //Вспомогательный метод для поиска идентификатора смартфона из url
    public String parseId(WebElement url) {
        Matcher idMatcher = Pattern.compile("(?<=\\/)\\d+").matcher(url.getAttribute("href"));
        String id ="notFound";
        if (idMatcher.find()) {
            id = idMatcher.group();
        }
        return id;
    }

}
