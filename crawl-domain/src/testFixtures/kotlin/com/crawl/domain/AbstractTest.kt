package com.crawl.domain

abstract class AbstractTest {

    protected fun buildSomeWebPageContent(bodyContent: String?): String {
        return buildDoctypeElementWithoutHtml() +
                buildHtmlContentWithoutDoctype(bodyContent)
    }

    protected fun buildSomeWebPageContentWithoutHead(bodyContent: String?): String {
        return buildDoctypeElementWithoutHtml() +
                buildHtmlContentWithoutHead(bodyContent)
    }

    protected fun buildSomeWebPageContentWithoutBody(): String {
        return buildDoctypeElementWithoutHtml() +
                buildHtmlContentWithoutBody()
    }

    protected fun buildDoctypeElementWithoutHtml(): String {
        return "<!DOCTYPE html PUBLIC \"blá\">"
    }

    protected fun buildHtmlContentWithoutDoctype(bodyContent: String?): String {
        return String.format("<html>%s%s</html>", buildHeadElement(), buildBodyElement(bodyContent))
    }

    protected fun buildHtmlContentWithoutHead(bodyContent: String?): String {
        return String.format("<html>%s</html>", buildBodyElement(bodyContent))
    }

    protected fun buildHtmlContentWithoutBody(): String {
        return String.format("<html>%s</html>", buildHeadElement())
    }

    protected fun buildBodyElement(bodyContent: String?): String {
        return String.format("<body>%s</body>", bodyContent)
    }

    protected fun buildHeadElement(): String {
        return "<head></head>"
    }

    companion object {
        protected const val zero: Int = 0
        protected const val justOnce: Int = 1
        protected const val twice: Int = 2
        protected const val three: Int = 3
        protected const val four: Int = 4
        protected const val hasUrl: Boolean = true
        protected const val notFinished: Boolean = true
        protected const val partialResult: Boolean = true
        protected const val notPartialResult: Boolean = false
        protected const val defaultId: String = "a1b2c3d4"
        protected const val defaultKeyword: String = "security"
        protected const val defaultFileName: String = "fileName.html"
        protected const val myLink: String = "http://myurl.com"
        protected const val mySecureLink: String = "https://mysecurelink.com"
        protected const val someExternalLink: String = "http://someexternal.com/link"
        protected const val QUEUE_NAME = "FAKE_QUEUE_NAME"
    }

}