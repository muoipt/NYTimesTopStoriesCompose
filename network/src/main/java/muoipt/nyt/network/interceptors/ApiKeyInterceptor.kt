package muoipt.nyt.network.interceptors

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addEncodedPathSegment("home.json")
            .addEncodedQueryParameter("api-key", apiKey)
            .build()


        // Request customization: add request headers
        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)

        val request: Request = requestBuilder.build()
        return chain.proceed(request)

//        // add api key to the url of the chain request
//        return chain.proceed(
//            chain.request()
//                .newBuilder()
//                .addHeader("api-key", apiKey)
//                .addHeader("Content-Type", "application/json")
//                .
//                .build()
//        )
    }
}