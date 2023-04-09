package com.example.composeunit.retrofit

import android.annotation.SuppressLint
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

object RetrofitManger {
    private const val BASE_URL = "https://api.openai.com/v1/"
    private var retrofit: Retrofit? = null
    private const val READ_TIMEOUT = 50L
    private const val WRITE_TIMEOUT = 50L
    private const val CONNECT_TIMEOUT = 50L

    val service: ApiService by lazy {
        getRetrofitInstance().create(ApiService::class.java)
    }

    private fun getRetrofitInstance(): Retrofit {
        if (retrofit == null) {
            retrofit = getHttpClient()?.let {
                Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(it)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        }
        return retrofit!!
    }

    //获得无需验证任何证书的OkHttpClient实例对象
    private fun getHttpClient(): OkHttpClient? {
        try {
            return getSSLSocketFactory().let {
                it.first?.let { it1 ->
                    OkHttpClient.Builder()
                        .sslSocketFactory(it1, it.second as X509TrustManager)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .hostnameVerifier(HostnameVerifier { _, _ ->
                            return@HostnameVerifier true
                        }).build()
                }
            }
        } catch (e: Exception) {
            Log.e("OkHttpClientError", e.message!!)
        }
        return null
    }

    //客户端不对服务器证书做任何验证
    @SuppressLint("CustomX509TrustManager")
    @Throws(Exception::class)
    private fun getSSLSocketFactory(): Pair<SSLSocketFactory?, TrustManager?> {
        //创建一个不验证证书链的证书信任管理器。
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(
            null, trustAllCerts,
            SecureRandom()
        )
        // Create an ssl socket factory with our all-trusting manager
        return Pair(sslContext.socketFactory, trustAllCerts[0])
    }
}

