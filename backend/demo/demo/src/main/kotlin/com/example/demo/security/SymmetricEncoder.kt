package com.example.demo.security

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


const val KEY_ALIAS = "key.jks"
const val KEYSTORE_PATH = "backend/demo/demo/src/main/kotlin/com/example/demo/security/symmetricKeys/$KEY_ALIAS"
const val KEYSTORE_PASSWORD = "admin"

class SymmetricEncoder {

    //
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
            // Criar uma instância do Cipher para descriptografia AES
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

            // Inicializar o Cipher no modo de descriptografia com a chave simétrica
            cipher.init(Cipher.DECRYPT_MODE, secret)

            // Decodificar os dados criptografados a partir da representação em Base64
            val encryptedBytes = Base64.getDecoder().decode(data)

            // Descriptografar os bytes
            val decryptedBytes = cipher.doFinal(encryptedBytes)

            // Converter os bytes descriptografados de volta para uma string
            return String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    private fun generateKeyStore(){
        try{
            val keyStoreFile = File(KEYSTORE_PATH)

            if (!keyStoreFile.exists()) {

                // Criar um novo keystore se ele não existir
                val keyStore = KeyStore.getInstance("PKCS12")
                val password: CharArray = KEYSTORE_PASSWORD.toCharArray()
                keyStore.load(null, password) // Cria um keystore vazio

                // Gerar uma nova chave simétrica (AES)
                val keyGen: KeyGenerator = KeyGenerator.getInstance("AES")
                val secretKey: SecretKey = keyGen.generateKey()

                // Armazenar a chave no keystore
                val entryPassword: KeyStore.ProtectionParameter = KeyStore.PasswordProtection(password)
                val keyEntry = KeyStore.SecretKeyEntry(secretKey)
                keyStore.setEntry(KEY_ALIAS, keyEntry, entryPassword)

                FileOutputStream(KEYSTORE_PATH).use { fos ->
                    keyStore.store(fos, password)
                    println("Keystore criado e chave criptográfica armazenada com sucesso.")
                }

            } else {
                println("Keystore já existe. Não é necessário criar um novo.");
            }
        }  catch (e:Exception){
            e.printStackTrace();
        }
    }

    // Vai buscar a chave simétrica ao ficheiro KeyStore key.jks
    private fun getSecretKey(): SecretKey? {
        try {
            // Carrega o KeyStore a partir do ficheiro
            val keyStore = KeyStore.getInstance("JKS")
            val password: CharArray = KEYSTORE_PASSWORD.toCharArray()
            FileInputStream(KEYSTORE_PATH).use { fis ->
                keyStore.load(fis, password)
            }

            // Recuperar a chave simétrica do KeyStore
            val keyEntry = keyStore.getEntry(KEY_ALIAS, KeyStore.PasswordProtection(password)) as? KeyStore.SecretKeyEntry
            return keyEntry?.secretKey
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
