package com.memento.eventproducer

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
import java.nio.ByteBuffer

class AwsKmsTest {

    @Test
    fun encrypt() {
        val accessKey = System.getProperty("aws.key.access")
        val secretKey = System.getProperty("aws.key.secret")
        val kmsId = System.getProperty("kms.key")

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
                .withPlaintext(ByteBuffer.wrap("reason-secret".toByteArray()))
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