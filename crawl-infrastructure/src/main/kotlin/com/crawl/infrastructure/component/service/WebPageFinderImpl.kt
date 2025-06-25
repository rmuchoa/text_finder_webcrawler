package com.crawl.infrastructure.component.service

import com.crawl.application.port.output.WebPageExternalRepositoryPort
import com.crawl.application.port.output.WebPageInternalRepositoryPort
import com.crawl.application.service.WebPageFinder
import org.springframework.stereotype.Service

@Service
class WebPageFinderImpl(
    externalRepository: WebPageExternalRepositoryPort,
    internalRepository: WebPageInternalRepositoryPort
) : WebPageFinder(externalRepository, internalRepository)