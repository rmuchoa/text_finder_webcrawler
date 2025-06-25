package com.crawl.infrastructure.repository.file

import com.crawl.application.port.output.WebPageInternalRepositoryPort
import com.crawl.domain.entity.WebPageDocument
import com.crawl.domain.values.Url
import org.springframework.stereotype.Repository

@Repository
class WebPageFileSystemRepository : WebPageInternalRepositoryPort {

    override fun findWebPageByUrl(url: Url): WebPageDocument? {
        TODO("Not yet implemented")
    }

    override fun saveWebPage(webPage: WebPageDocument): WebPageDocument? {
        TODO("Not yet implemented")
    }

    override fun existsWebPageByUrl(url: Url): Boolean {
        return false
    }
}