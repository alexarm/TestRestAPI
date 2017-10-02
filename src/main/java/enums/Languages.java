package enums;

public enum Languages {
        RU("ru"),
        EN("en"),
        ERR("lu");

        public String languageCode;

        private Languages(String lang) {
            this.languageCode = lang;
        }
}
