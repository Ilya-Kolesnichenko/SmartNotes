package com.ilyakoles.smartnotes.utils

import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSACrypt {

    val transformation = "RSA"
    val publicKeyStr =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC039jg7sotX4xr+LGdmTWs7TgRGTAiMAINpAX8B1r8qUbiyHpqp4ozlQhOI8ogMM+p1rcDWTvM+8Iwd9laClFUeVYaun+h4XUgIM5nQ1qmTVN3uf1lYZxzf2a8B0pHWxPYDwIyeHj2UEb3Cx5i5NG5cZ24depXP6jPKwyzTTJtEwIDAQAB"
    val privateKeyStr =
        "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALTf2ODuyi1fjGv4sZ2ZNaztOBEZMCIwAg2kBfwHWvypRuLIemqnijOVCE4jyiAwz6nWtwNZO8z7wjB32VoKUVR5Vhq6f6HhdSAgzmdDWqZNU3e5/WVhnHN/ZrwHSkdbE9gPAjJ4ePZQRvcLHmLk0blxnbh16lc/qM8rDLNNMm0TAgMBAAECgYAKlYrAZtjH3O5/pvblzQBaFSuRvJKXfY2xNKbw/5EwdctjG+4l7ZXlvNPWlruONK0COEFPXdpk/Vp4sZqzbSUjHcirJp4NifP+RuJAiAYzqkVT7kPykC9+id4JPsyLmKRt7bLc30vCtdFCADlYW0/vHHxMo5bENQb1ssmWSA9QgQJBAP50eLzPGQRhzeQqcJEDEK1xNqj3bJ2sL5nKi4BpHoORoqjnJkxXOsiunkh2vOLW1Hv/rRvuSv4BPQ61qmJwnNMCQQC1+QA6WuEchcnM/kDof0HAIFJQ6iWdavoLFldSW8Jt5xoWjJ/BBEs2KGnAGFtEPzjGIM5pthqONbUbQLwKW8bBAkB8yYncroPKTly2pMmHlEU9ieQQgSbXPHYrqdU4KFU6mNV4l8OEdNLzUA934iNH66tRFFZE+Fv2rYzQBe+FT0zZAkBR9I8RuRRhkC/Oz0PUclved7AbGRlPyHpMvAcf5Iuwi8DIHxVkDNcC0Tivd0jDd+XN9cCBA676FV43o/QMhkEBAkAVQiJlcrVNJHfG3/94VV3vs8iCwcFiMn14Rij7YqhkpdaY6rEM17Wttc/jowkkJ4bk7mmDJOHWyyPLYhJq4tiV"
    val ENCRYPT_MAX_SIZE = 117 //Encryption: The maximum encryption length each time
    val DECRYPT_MAX_SIZE = 128 //Decryption: maximum encryption length each time

    val KEY_PRIVATE = 0
    val KEY_PUBLIC = 1

    fun getPrivateKey(): PrivateKey {

        //The string is converted into a key pair object
        val kf = KeyFactory.getInstance("RSA")
        val privateKey = kf.generatePrivate(PKCS8EncodedKeySpec(Base64.decode(privateKeyStr)))
        return privateKey
    }

    fun getPublicKey(): PublicKey {

        //The string is converted into a key pair object
        val kf = KeyFactory.getInstance("RSA")
        val publicKey = kf.generatePublic(X509EncodedKeySpec(Base64.decode(publicKeyStr)))
        return publicKey
    }

    /**
     * Private key encryption
     * @param input
     * @param privateKey private key
     */
    fun encryptByPrivateKey(input: String, privateKey: PrivateKey): String {
        //********************Asymmetric encryption trilogy********************//
        val byteArray = input.toByteArray()

        //1. Create a cipher object
        val cipher = Cipher.getInstance(transformation)
        //2. Initialize cipher
        cipher.init(Cipher.ENCRYPT_MODE, privateKey)
        //3. Encryption: Segment encryption
        //val encrypt = cipher.doFinal(input.toByteArray())

        var temp: ByteArray? = null
        var offset = 0 //Current offset position

        val bos = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) { //Not encrypted
            //Maximum encryption of 117 bytes at a time
            if (byteArray.size - offset >= ENCRYPT_MAX_SIZE) {
                //The remaining part is greater than 117
                //Encrypt complete 117
                temp = cipher.doFinal(byteArray, offset, ENCRYPT_MAX_SIZE)
                //Recalculate the offset position
                offset += ENCRYPT_MAX_SIZE
            } else {
                //Encrypt the last piece
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //Recalculate the offset position
                offset = byteArray.size
            }
            //Store to temporary buffer
            bos.write(temp)
        }
        bos.close()

        return Base64.encode(bos.toByteArray())
    }

    //Public key encryption
    fun encryptByPublicKey(input: String, publicKey: PublicKey): String {
        //********************Asymmetric encryption trilogy********************//

        val byteArray = input.toByteArray()

        //1. Create a cipher object
        val cipher = Cipher.getInstance(transformation)
        //2. Initialize cipher
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        //3. Encryption: Segment encryption
        //val encrypt = cipher.doFinal(input.toByteArray())

        var temp: ByteArray? = null
        var offset = 0 //Current offset position

        val bos = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) { //Not encrypted
            //Maximum encryption of 117 bytes at a time
            if (byteArray.size - offset >= ENCRYPT_MAX_SIZE) {
                //The remaining part is greater than 117
                //Encrypt complete 117
                temp = cipher.doFinal(byteArray, offset, ENCRYPT_MAX_SIZE)
                //Recalculate the offset position
                offset += ENCRYPT_MAX_SIZE
            } else {
                //Encrypt the last piece
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //Recalculate the offset position
                offset = byteArray.size
            }
            //Store to temporary buffer
            bos.write(temp)
        }
        bos.close()

        return Base64.encode(bos.toByteArray())
    }

    /**
     * Private key decryption
     * @param input ciphertext
     */
    fun decryptByPrivateKey(input: String, privateKey: PrivateKey): String {
        //********************Asymmetric encryption trilogy********************//

        val byteArray = Base64.decode(input)

        //1. Create a cipher object
        val cipher = Cipher.getInstance(transformation)
        //2. Initialize cipher
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        //3. Segment decryption
        var temp: ByteArray? = null
        var offset = 0 //Current offset position

        val bos = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) { //Not encrypted
            //The maximum decryption is 128 bytes at a time
            if (byteArray.size - offset >= DECRYPT_MAX_SIZE) {
                temp = cipher.doFinal(byteArray, offset, DECRYPT_MAX_SIZE)
                //Recalculate the offset position
                offset += DECRYPT_MAX_SIZE
            } else {
                //Encrypt the last piece
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //Recalculate the offset position
                offset = byteArray.size
            }
            //Store to temporary buffer
            bos.write(temp)
        }
        bos.close()

        return String(bos.toByteArray())
    }

    /**
     * Private key decryption
     * @param input ciphertext
     */
    fun decryptByPublicKey(input: String, publicKey: PublicKey): String {
        //********************Asymmetric encryption trilogy********************//

        val byteArray = Base64.decode(input)

        //1. Create a cipher object
        val cipher = Cipher.getInstance(transformation)
        //2. Initialize cipher
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        //3. Segment decryption
        var temp: ByteArray? = null
        var offset = 0 //Current offset position

        val bos = ByteArrayOutputStream()

        while (byteArray.size - offset > 0) { //Not encrypted
            //The maximum decryption is 128 bytes at a time
            if (byteArray.size - offset >= DECRYPT_MAX_SIZE) {
                temp = cipher.doFinal(byteArray, offset, DECRYPT_MAX_SIZE)
                //Recalculate the offset position
                offset += DECRYPT_MAX_SIZE
            } else {
                //Encrypt the last piece
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //Recalculate the offset position
                offset = byteArray.size
            }
            //Store to temporary buffer
            bos.write(temp)
        }
        bos.close()
        return String(bos.toByteArray())
    }

    fun getEncryptStr(string: String, typeKey: Int): String {
        val kf = KeyFactory.getInstance("RSA")
        val privateKey = kf.generatePrivate(PKCS8EncodedKeySpec(Base64.decode(privateKeyStr)))
        val publicKey = kf.generatePublic(X509EncodedKeySpec(Base64.decode(publicKeyStr)))

        var vResult = ""

        if (typeKey == 0) {
            vResult = encryptByPrivateKey(string, privateKey)
        } else {
            vResult = encryptByPublicKey(string, publicKey)
        }

        return vResult
    }
}