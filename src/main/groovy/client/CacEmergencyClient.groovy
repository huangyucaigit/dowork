package client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder
import pojo.SynResult

class CacEmergencyClient implements ISendClient{

    SynResult send(def data){
        String baseUrl = "http://localhost:8888/"
        HttpClient client = HttpClient.create(baseUrl.toURL())
        HttpRequest request = HttpRequest.POST(UriBuilder.of('/cacEmergency/receive').build(),data)
        HttpResponse<String> resp = client.toBlocking().exchange(request, String) // <1>
        String json = resp.body()
        ObjectMapper objectMapper = new ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // <2>
        SynResult synResult = objectMapper.readValue(json, SynResult) // <3>
        return synResult
    }
}



