package com.example.project_t2.network

import android.net.http.HttpResponseCache.install
import com.example.project_t2.models.KoBERT.KoBERTRequestBody
import com.example.project_t2.models.KoBERT.KoBERTResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

suspend fun getKoBERTResponse(text: String) : KoBERTResponse {
    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    val url = "http://3.24.124.212/predict"

    val response: HttpResponse = client.post(url) {
        contentType(ContentType.Application.Json)
        setBody(KoBERTRequestBody(text = text))
    }

    client.close()

    when (response.status.value) {
        200 -> {
            return response.body<KoBERTResponse>()
        }
        else -> {
            throw Exception("Error: ${response.status.value} - ${response.status.description}")
        }
    }
}