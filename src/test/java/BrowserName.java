public enum BrowserName {

    CHROME("chrome"),
    FIREFOX("firefox"),
    OPERA("opera"),
    DEFAULT("firefox");

    String browserName;

    BrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    //Возвращаем подходящий браузера, поиск регистронезависим
    public static BrowserName findByName(String name) {
        for (BrowserName value: values()) {
            if (value.browserName.equals(name.replace("'","").toLowerCase())) {
                return value;
            }
        }

        // Возвращаем браузер по умолчанию Firefox если не нашлось подходящего кандидата
        return DEFAULT;
    }
}
