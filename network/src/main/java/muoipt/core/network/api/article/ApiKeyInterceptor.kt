package muoipt.core.network.api.article

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // add api key to the url of the chain request
        return chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("api-key", apiKey)
                .addHeader("Content-Type", "application/json")
                .build()
        )
    }
}