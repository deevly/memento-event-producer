package com.server.mementoeventproducer

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.AWSKMSClientBuilder
import com.amazonaws.services.kms.model.DecryptRequest
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

    @Test
    fun decrypt() {
        val encrytedUsername = "gUTHn64Hn9Htd3/7lipBSSj7plUuzBfF/L5PtgqKD9kT5iyaY3pub2XUXQhMLgS84sZKPKaJDI23W6yjuE74pb7WVA8Gp2Im/vbmQgt1dYrfbwCYrALFZZTvliTEqGrrMrCgnc8dl/K28F2a+0dEozmFYSL/0o+o7SGWds5IbT8pOeGsR61zf2Ccb2BsxFZoX65Fq9h2zZaXbLScQTrdP5cbXhuz0PdGxDr2+ko0QeXF7HGw20IBB0LlHon9gZ6rZw34SxJ/H35IPaCjQ5Xg3dNnQ89CXuhrVtNB7cB5Wqpa0kFXXPRvsxSmIVZFr6TBZMS8lSnvQvAwVJv+ZkIk8w=="
        val encrytedSecret = "crjz4kI8RavxxsFQKr7NQFOBnmvPRdx1m9MhKm2LqCamUECCqCl1MQvAdZkTzSloBuYW4Pzew5fqZvPcONTjKG8b3dHk39PFQA7LoU4ldAcwbjeVl4XjuoLfn2oj5qxaqWWBxF6PKeqzIXvsNe4+2JZnUxr5xjM0d8EJD1p//XGzcMCC9wmbBdA0A91m75mi/0/GUgWIdDhaG1LpCpEMh4b79Y0cGwvE4IsrQvP+cyMcrJ9i30NNeTLwdZKFb/42R1SHt13zmlpslG5tGSMy/4IEI7nWAAv+6ZVwxe+rOIQToDtwPYNx7o2bK28iPZOdTtUqM21VgRTUUoPzxIRupA=="
        try {
            val awsCredentials: AWSCredentials = BasicAWSCredentials("AKIARFNIOFV2BPGRHMLI", "/pBKGKB+jtaEXAtRjH4S28kCkV7AzGfTUKmIcGM+")
            val kmsClient: AWSKMS = AWSKMSClientBuilder
                .standard()
                .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build()

            val usernameDecryptRequest: DecryptRequest = DecryptRequest()
                .withCiphertextBlob(ByteBuffer.wrap(Base64.decodeBase64(encrytedUsername)))
                .withKeyId("21fc1e9e-a4c9-4bb6-a4c9-7b5a5cff8e64")
                .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);

            val secretDecryptRequest: DecryptRequest = DecryptRequest()
                .withCiphertextBlob(ByteBuffer.wrap(Base64.decodeBase64(encrytedSecret)))
                .withKeyId("21fc1e9e-a4c9-4bb6-a4c9-7b5a5cff8e64")
                .withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);

            val usernamePlain: ByteBuffer = kmsClient.decrypt(usernameDecryptRequest).plaintext;
            val passwordPlain: ByteBuffer = kmsClient.decrypt(secretDecryptRequest).plaintext;


            System.out.println("plainText username: " + String(usernamePlain.array()));
            System.out.println("plainText password: " + String(passwordPlain.array()));
        } catch (e : Exception) {
            System.out.println(e.message);
        }
    }
}