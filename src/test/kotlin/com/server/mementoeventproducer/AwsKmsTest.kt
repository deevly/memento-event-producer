package com.server.mementoeventproducer

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.AWSKMSClientBuilder
import com.amazonaws.services.kms.model.EncryptRequest
import com.amazonaws.services.kms.model.EncryptResult
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec
import org.apache.commons.codec.binary.Base64
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.nio.ByteBuffer

@ActiveProfiles("test-credentials")
@SpringBootTest
class AwsKmsTest {

    @Value("\${accessKey}")
    lateinit var accessKey : String

    @Value("\${secretKey}")
    lateinit var secretKey : String

    @Value("\${memento.kms.key-id}")
    lateinit var kmsId : String

    @Test
    fun encrypt() {
        try {
            val awsCredentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
            val kmsClient: AWSKMS = AWSKMSClientBuilder
                .standard()
                .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build()

            var usernameEncryptRequest: EncryptRequest = EncryptRequest()
                .withKeyId(kmsId)
                .withPlaintext(ByteBuffer.wrap("reason".toByteArray()))
                .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)

            var passwordEncryptRequest: EncryptRequest = EncryptRequest()
                .withKeyId(kmsId)
                .withPlaintext(ByteBuffer.wrap("admin-reason".toByteArray()))
                .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)

            val usernameEncryptResult: EncryptResult = kmsClient.encrypt(usernameEncryptRequest)
            val usernameCipherTextBlob: ByteBuffer = usernameEncryptResult.ciphertextBlob

            val passwordEncryptResult: EncryptResult = kmsClient.encrypt(passwordEncryptRequest)
            val passwordCiphertextBlob: ByteBuffer = passwordEncryptResult.ciphertextBlob

            println("ciphertextBlob: " + String(Base64.encodeBase64(usernameCipherTextBlob.array())))
            println("ciphertextBlob: " + String(Base64.encodeBase64(passwordCiphertextBlob.array())))
        } catch (e : Exception) {
            println(e.message)
        }
    }
}