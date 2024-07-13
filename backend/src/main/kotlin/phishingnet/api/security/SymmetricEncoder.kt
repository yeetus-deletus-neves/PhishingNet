package phishingnet.api.security

import jdk.jfr.internal.SecuritySupport.getResourceAsStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


const val KEY_ALIAS = "key.jks"
const val KEYSTORE_RESOURCE_PATH = "/symmetricKeys/$KEY_ALIAS"
const val KEYSTORE_PASSWORD = "admin"

class SymmetricEncoder {

    private val secret: SecretKey?

    init {
        generateKeyStore()
        secret = getSecretKey()
        if (secret == null) throw RuntimeException("Secret Key is null")
    }

    // Codifica data utilizando uma criptografia AES simetrica
    fun encode(data: String): String? {
        try {
            // Criar uma instância do Cipher para criptografia AES
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

            // Inicializar o Cipher no modo de criptografia com a chave simétrica
            cipher.init(Cipher.ENCRYPT_MODE, secret)

            // Criptografar os dados
            val encryptedBytes = cipher.doFinal(data.toByteArray())

            // Converter os bytes criptografados para uma representação em Base64
            return Base64.getEncoder().encodeToString(encryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun decode(data: String): String? {
        try {
            // Cria uma instância do Cipher para desencriptar AES
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

            // Inicializa o Cipher no modo de desencriptação com a chave simétrica
            cipher.init(Cipher.DECRYPT_MODE, secret)

            // Decodifica os dados criptografados a partir da representação em Base64
            val encryptedBytes = Base64.getDecoder().decode(data)

            // Descriptografar os bytes
            val decryptedBytes = cipher.doFinal(encryptedBytes)

            // Converte os bytes desencriptados para uma string
            return String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    private fun generateKeyStore() {
        try {
            try {
                // Carrega o KeyStore a partir do recurso embutido
                val keyStore = KeyStore.getInstance("PKCS12")
                val password: CharArray = KEYSTORE_PASSWORD.toCharArray()

                getResourceAsStream(KEYSTORE_RESOURCE_PATH)?.use { inputStream ->
                    keyStore.load(inputStream, password)
                }

                // Verifica se o KeyStore já existe
                if (keyStore.containsAlias(KEY_ALIAS)) {
                    println("Keystore já existe. Não é necessário criar um novo.")
                    return
                }

                // Gera uma nova chave simétrica (AES)
                val keyGen: KeyGenerator = KeyGenerator.getInstance("AES")
                val secretKey: SecretKey = keyGen.generateKey()

                // Armazena a chave no KeyStore
                val entryPassword: KeyStore.ProtectionParameter = KeyStore.PasswordProtection(password)
                val keyEntry = KeyStore.SecretKeyEntry(secretKey)
                keyStore.setEntry(KEY_ALIAS, keyEntry, entryPassword)

                // Salva o KeyStore de volta ao recurso embutido (opcional)
                // getResourceAsStream(KEYSTORE_RESOURCE_PATH)?.use { inputStream ->
                //     keyStore.store(inputStream, password)
                // }

                println("KeyStore criado e chave criptográfica armazenada com sucesso.")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    // Vai buscar a chave simétrica ao ficheiro KeyStore key.jks
    private fun getSecretKey(): SecretKey? {
        try {

            // Carrega o KeyStore a partir do ficheiro
            val keyStore = KeyStore.getInstance("JKS")
            val password: CharArray = KEYSTORE_PASSWORD.toCharArray()
            getResourceAsStream(KEYSTORE_RESOURCE_PATH)?.use { fis ->
                keyStore.load(fis, password)
            }

            // Recupera a chave simétrica do KeyStore
            val keyEntry = keyStore.getEntry(KEY_ALIAS, KeyStore.PasswordProtection(password)) as? KeyStore.SecretKeyEntry
            return keyEntry?.secretKey
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // Obtém um stream do recurso
    private fun getResourceAsStream(resourcePath: String): InputStream? {
        return javaClass.getResourceAsStream(resourcePath)
    }

    // Obtém um arquivo como recurso embutido
    private fun getResourceAsFile(resourcePath: String): File {
        return File(javaClass.getResource(resourcePath).file)
    }
}