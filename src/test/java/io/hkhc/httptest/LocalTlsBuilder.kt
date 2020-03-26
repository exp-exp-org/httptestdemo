package io.hkhc.httptest

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import java.net.InetAddress

class LocalTlsBuilder {

    private val localhost = InetAddress.getByName("localhost").canonicalHostName

    /**
        Ceeate key pair and certificate on the fly in memory. Both client and server will use them to handshake
        during unit tests.
     */
    private val localhostCertificate = HeldCertificate.Builder()
        .addSubjectAlternativeName(localhost)
        .build()

    /**
        Create socket factory for server TLS connection
     */
    private var serverCertificates = HandshakeCertificates.Builder()
        .heldCertificate(localhostCertificate)
        .build()

    /**
        Create client socket factory and trusted manager for client TLS connection
     */
    private var clientCertificaes = HandshakeCertificates.Builder()
        .addTrustedCertificate(localhostCertificate.certificate())
        .build()

    /**
        Helper to setup OkHttp client builder to use the local certificate
     */
    fun setupLocalTrust(builder: OkHttpClient.Builder) {
        builder.sslSocketFactory(clientCertificaes.sslSocketFactory(), clientCertificaes.trustManager())
    }

    /**
        Helper to setup MockWebServer instance to use the local certificate
     */
    fun setupMockWebServer(mockWebServer: MockWebServer, tunnelProxy: Boolean = false) {
        mockWebServer.useHttps(serverCertificates.sslSocketFactory(), tunnelProxy)
    }

}

/*
    Extension to simplify the use of LcoalTlsManager
 */

fun OkHttpClient.Builder.localTrust(tlsBuilder: LocalTlsBuilder): OkHttpClient.Builder {
    tlsBuilder.setupLocalTrust(this)
    return this
}

fun MockWebServer.localTrust(tlsBuilder: LocalTlsBuilder, tunnelProxy: Boolean = false): MockWebServer {
    tlsBuilder.setupMockWebServer(this, tunnelProxy)
    return this
}