import config.ServerConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Домашнее задание №3
 * Тест Яндекс Маркета
 * Александр Кремлёв
 * 1.0
 */

public class Task3Test extends Assert {
    private final Logger logger = LogManager.getLogger(Task3Test.class);
    protected static WebDriver driver;
    private final ServerConfig cfg = ConfigFactory.create(ServerConfig.class);
    private String samsungId;
    private String xiaomiId;


    @Before
    public void Setup() {

        //Получаем имя браузера из параметра -Dbrowser командной строки, если не указан то по умолчанию chrome
        String name = Optional.ofNullable(System.getProperty("browser")).orElse("chrome");

        //Получаем имя драйвера из класса Enum
        BrowserName browserName = BrowserName.findByName(name);

        //Если имя браузера не было распознано корректно, то логируем предупреждение
        if (browserName == BrowserName.DEFAULT) {
            logger.warn("WebDriver name from the cmdline is not recognized %" + name
                    + "% use Firefox");
        }

        //Создаём вебдрайвер через статический метод класса WebDriverFactory
        driver = WebDriverFactory.create(browserName);
        logger.info("Start WebDriver " + browserName.getBrowserName());

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void yandexTest() {
        logger.info("Run Test for Yandex Market");

        driver.manage().window().maximize();

        //Открываем главную страницу маркета
        driver.get(cfg.urlYandex());

        //Соглашаемся со всем
        try {
            driver.findElement(By.xpath(cfg.alert())).click();
        } catch (WebDriverException we) {
            //do nothing
        }

        //Переходим в раздел Электроника
        driver.findElement(By.cssSelector(cfg.electronicChapter())).click();

        //Переходим в раздел смартфоны
        driver.findElement(By.cssSelector(cfg.phoneChapter())).click();

        //Соглашаемся со всем
        try {
            driver.findElement(By.xpath(cfg.alert())).click();
        } catch (WebDriverException we) {
            //do nothing
        }

        //Отмечаем чекбоксы Самсунг и Сяоми
        driver.findElement(By.cssSelector(cfg.samsungBox())).click();
        driver.findElement(By.cssSelector(cfg.xiaomiBox())).click();

        //Сортируем по цене
        driver.findElement(By.cssSelector(cfg.sortByPrice())).click();

        //ждём пока прогрузится новый список с элементами
        new WebDriverWait(driver, 3).until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector(cfg.firstSamsung()))));

        //Ищем первый Самсунг
        WebElement samsungUrl;
        samsungUrl = driver.findElement(By.cssSelector(cfg.firstSamsung()));

        //запоминаем идентификатор
        samsungId = parseId(samsungUrl);

        //добавляем к сравнению
        String idSelector = "article[data-zone-data*='" + samsungId + "'] div[aria-label*='сравнению']";
        driver.findElement(By.cssSelector(idSelector)).click();
        WebElement sams = driver.findElement(By.xpath(cfg.add()));

        //Проверяем что телефон добавлен
        Assert.assertTrue(sams.getText().contains("Samsung"));
        logger.info("Samsung added " + sams.getText());

        //Ищем первый Сяоми
        WebElement xiaomiUrl;
        xiaomiUrl = driver.findElement(By.cssSelector(cfg.firstXiaomi()));

        //запоминаем идентификатор
        xiaomiId = parseId(xiaomiUrl);

        //добавляем к сравнению
        idSelector = "article[data-zone-data*='" + xiaomiId + "'] div[aria-label*='сравнению']";
        driver.findElement(By.cssSelector(idSelector)).click();
        WebElement xia = driver.findElement(By.xpath(cfg.add()));

        //Проверяем что телефон добавлен
        Assert.assertTrue(xia.getText().contains("Xiaomi"));
        logger.info("Xiaomi added " + xia.getText());

        //Сравниваем
        driver.get(cfg.compare());

        //Проверяем по идентификатору, что выбранные ранее телефоны добавлены к сравнению
        WebElement samsung = driver.findElement(By.cssSelector("a[href*='/" + samsungId +"']"));
        Assert.assertTrue(samsung.isDisplayed());
        logger.info("Samsung find " + samsung.getText());

        WebElement xiaomi = driver.findElement(By.cssSelector("a[href*='/" + xiaomiId +"']"));
        Assert.assertTrue(xiaomi.isDisplayed());
        logger.info("Xiaomi find " + xiaomi.getText());

        //Тест пройден
        logger.info("Test is passed");
    }

    //Вспомогательный метод для поиска идентификатора смартфона из url
    private String parseId(WebElement url) {
        Matcher idMatcher = Pattern.compile("(?<=\\/)\\d+").matcher(url.getAttribute("href"));
        String id ="notFound";
        if (idMatcher.find()) {
            id = idMatcher.group();
        }
        return id;
    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.close();
            logger.info("Shutdown WebDriver");
        } else {
            logger.error("Error WebDriver not found");
        }
    }
}
