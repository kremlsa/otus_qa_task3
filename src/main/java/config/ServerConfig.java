package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("classpath:config.properties")
public interface ServerConfig extends Config {

    @Key("url.yandex")
    String urlYandex();

    @Key("electronicChapter")
    String electronicChapter();

    @Key("phoneChapter")
    String phoneChapter();

    @Key("samsungBox")
    String samsungBox();

    @Key("xiaomiBox")
    String xiaomiBox();

    @Key("sortByPrice")
    String sortByPrice();

    @Key("alert")
    String alert();

    @Key("firstSamsung")
    String firstSamsung();

    @Key("firstXiaomi")
    String firstXiaomi();

    @Key("compare")
    String compare();

}