package com.server.mementoeventproducer.application.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.AWSKMSClientBuilder
import com.amazonaws.services.kms.model.AWSKMSException
import com.amazonaws.services.kms.model.DecryptRequest
import com.amazonaws.services.kms.model.EncryptionAlgorithmSpec
import mu.KLogging
import org.apache.commons.codec.binary.Base64
import java.nio.ByteBuffer

class AwsDecryptor {

    companion object {
        private val logger = KLogging().logger

        fun getPlainText(encryptedText: String, keyId: String) : String {

            val accessKey = System.getProperty("aws.key.access")
            val secretKey = System.getProperty("aws.key.secret")

            try {
                val awsCredentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
                val kmsClient: AWSKMS = AWSKMSClientBuilder
                    .standard()
                    .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_NORTHEAST_2)
                    .build()

                val usernameDecryptRequest: DecryptRequest = DecryptRequest()
                    .withCiphertextBlob(ByteBuffer.wrap(Base64.decodeBase64(encryptedText)))
                    .withKeyId(keyId)
                    .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)

                val plainText: ByteBuffer = kmsClient.decrypt(usernameDecryptRequest).plaintext

                return String(plainText.array())
            } catch (e : AWSKMSException) {
                logger.error { String.format("KMS decryption exception : ${e.message}") }
                return ""
            }
        }
    }
}