package org.bmserver.app.common.config.elastic

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration
import org.springframework.data.elasticsearch.support.HttpHeaders

@Configuration
class ElasticConfig : ReactiveElasticsearchConfiguration() {
    @Value("\${elasticsearch.host}")
    lateinit var elasticsearchHost: String

    @Value("\${elasticsearch.port}")
    lateinit var elasticsearchPort: String

    @Value("\${elasticsearch.api-key}")
    lateinit var elasticsearchApiKey: String


    override fun clientConfiguration(): ClientConfiguration {
        val authHeader = HttpHeaders()
            .also { it.add(HttpHeaders.AUTHORIZATION, "ApiKey $elasticsearchApiKey") }

        return ClientConfiguration.builder()
            .connectedTo("$elasticsearchHost:$elasticsearchPort")
            .withConnectTimeout(0)
            .withSocketTimeout(0)
            .withDefaultHeaders(authHeader)
            .build()
    }
}