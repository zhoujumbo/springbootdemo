package com.java;/**
 * @Description： TODO
 * @Author: zhoujumbo
 * @Date: ${Time} ${Date}
 */

import com.fortunetree.basic.support.commons.business.encrypt.AESUtil;
import com.fortunetree.basic.support.commons.business.encrypt.CipherAesException;
import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @ClassName AESTest
 * @Description AES 算法 对称加密，密码学中的高级加密标准 2005年成为有效标准
 * @Author jb.zhou
 * @Date 2019/9/25
 * @Version 1.0
 */
public class AESTest {

    static Cipher cipher;
    // 默认模式
    static final String ALGORITHM_ECB = "AES/ECB/PKCS5Padding"; // no IV 111-152
    // 待加密内容的字节长度必须是16的倍数
    static final String ALGORITHM_ECB_NOPADDING = "AES/ECB/NoPadding";  // no IV
    static final String ALGORITHM_CBC = "AES/CBC/PKCS5Padding"; // use IV   111-152
    /*
     * AES/CBC/NoPadding 要求
     * 密钥必须是16字节长度的；Initialization vector (IV) 必须是16字节
     * 待加密内容的字节长度必须是16的倍数，如果不是16的倍数，就会出如下异常：
     * javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes
     *
     *  由于固定了位数，所以对于被加密数据有中文的, 加、解密不完整
     *
     *  可 以看到，在原始数据长度为16的整数n倍时，假如原始数据长度等于16*n，则使用NoPadding时加密后数据长度等于16*n，
     *  其它情况下加密数据长 度等于16*(n+1)。在不足16的整数倍的情况下，假如原始数据长度等于16*n+m[其中m小于16]，
     *  除了NoPadding填充之外的任何方 式，加密数据长度都等于16*(n+1).
     */
    static final String ALGORITHM_CBC_NoPadding = "AES/CBC/NoPadding";

    static final String ALGORITHM_CFB_PKCS5Padding = "AES/CFB/PKCS5Padding";  // use IV  111-152
    static final String ALGORITHM_CFB_NOPADDING = "AES/CFB/NoPadding";  // use IV    111-148
    static final String ALGORITHM_OFB_PKCS5Padding = "AES/OFB/PKCS5Padding";  // use IV
    static final String ALGORITHM_OFB_NOPADDING = "AES/OFB/NoPadding";  // use IV
    static final String KEY_ALGORITHM = "AES";

    static final String encryptKey = "300113";
    static final String IV = "1003311003311003";

    /**
     * IV生成器
     *
     * @return
     */
    static byte[] getIV() throws NoSuchAlgorithmException {
        return IV.getBytes();
    }

    static byte[] getIV(String iv) {
        if (isEmpty(iv) || iv.length() != 16) {
            throw new CipherAesException("IV错误");
        }
        return iv.getBytes();
    }

    /**
     * SecretKey生成器
     *
     * @param keySeed
     * @return
     */
    public static SecretKey getKey(String keySeed, String algorithm) {
        // 可根据系统环境变量动态指定
        //if (keySeed == null) {
        //    keySeed = System.getenv("AES_SYS_KEY");
        //}
        //if (keySeed == null) {
        //    keySeed = System.getProperty("AES_SYS_KEY");
        //}
        if (isEmpty(keySeed)) {
//            keySeed = "acb#$123";// 默认种子
            keySeed = "customer2019!@#$";// 默认种子
        }
        if (isEmpty(algorithm)) {
            algorithm = "AES";
        }
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator generator = KeyGenerator.getInstance(algorithm);
            //2.根据keySeed初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            // SHA1PRNG SUN提供程序提供的伪随机数生成（PRNG）算法 使用SHA-1作为PRNG的基础
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            // KEY_LENGTH 128
            generator.init(128, secureRandom);
            //3.产生原始对称密钥
            SecretKey secretKey = generator.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = secretKey.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, algorithm);
            return key;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    String str0 = "a*jal)kzx囙国为国宽"; // 14
    String str1 = "123456781234囙为国宽"; // 16
    String str2 = "a*jal)k32J8czx囙国为国宽"; // 19
    String str3 = "12345678abcdef"; // 14
    String str4 = "12345678abcdefgh"; // 16
    String str5 = "12345678abcdefghijk"; // 19

    /**
     * 使用AES 算法 加密，默认模式 AES/ECB/PKCS5Padding
     */
    @Test
    public void method1() throws Exception {
//        String content = str2;
        String content = "12345678abcdefgh12345678abcdefgh12345678abcdefgh";
        System.out.println("原始数据长度:" + content.length());
        try {
            /*
             * 加密
             */
            //5.生成AES密钥
            SecretKey key = getKey(encryptKey, (String) null);
            System.out.println("密钥的长度为：" + key.getEncoded().length);
            //6.根据指定算法AES获取密码器
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY，第三个参数为IV偏移量（特定算法需要）
            cipher.init(Cipher.ENCRYPT_MODE, key); //, new IvParameterSpec(getIV())
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode = new String(base64Encode(byte_AES));
            System.out.println("method1-加密：" + AES_encode);
            System.out.println("method1-加密后长度：" + AES_encode.length());
            /*
             * 解密
             */
            cipher.init(Cipher.DECRYPT_MODE, key);//使用解密模式初始化 密钥
            byte[] byte_content = new BASE64Decoder().decodeBuffer(AES_encode);
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            System.out.println("method1-解密后：" + AES_decode);
            System.out.println("method1-解密后长度：" + AES_decode.length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 使用AES/ECB/NoPadding 或者 AES/CBC/NoPadding算法 加密，
     * 密钥必须是16字节长度的
     * Initialization vector (IV) 必须是16字节
     * 待加密内容的字节长度必须是16的倍数,否则：javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes
     * 由于固定了位数，所以对于被加密数据有中文的, 加、解密不完整
     */
    @Test
    public void method2() throws Exception {
        String content = str1;
        System.out.println("原始数据长度:" + content.length());
        try {
            /*
             * 加密
             */
            SecretKey key = getKey(encryptKey, (String) null);
            System.out.println("密钥的长度为：" + key.getEncoded().length);

            Cipher cipher = Cipher.getInstance(ALGORITHM_ECB_NOPADDING);
//            Cipher cipher = Cipher.getInstance(ALGORITHM_CBC_NoPadding);
            cipher.init(Cipher.ENCRYPT_MODE, key); //, new IvParameterSpec(getIV())
            byte[] byte_encode = content.getBytes("utf-8");
            System.out.println("待加密数组长度:" + byte_encode.length);
            byte[] byte_AES = cipher.doFinal(byte_encode);
            String AES_encode = new String(base64Encode(byte_AES));
            System.out.println("method1-加密：" + AES_encode);
            System.out.println("method1-加密后长度：" + AES_encode.length());
            /*
             * 解密
             */
            cipher.init(Cipher.DECRYPT_MODE, key);//使用解密模式初始化 密钥
            byte[] byte_content = new BASE64Decoder().decodeBuffer(AES_encode);
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            System.out.println("method1-解密后：" + AES_decode);
            System.out.println("method1-解密后长度：" + AES_decode.length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    /**
     * 使用AES/CBC/PKCS5Padding 算法 加密，
     * use IV
     */
    @Test
    public void method3() throws Exception {
        String str = "a1";
        String content = "a1";
        System.out.println("原始数据:" + content);
        System.out.println("原始数据长度:" + content.length());
        try {
            /*
             * 加密
             */
            SecretKey key = getKey(encryptKey, (String) null);
            System.out.println("密钥的长度为：" + key.getEncoded().length);

            Cipher cipher = Cipher.getInstance(ALGORITHM_CBC);
//            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(getIV())); //, new IvParameterSpec(getIV())
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec("1111111111111111".getBytes())); //, new IvParameterSpec(getIV())
            byte[] byte_encode = content.getBytes("utf-8");
            System.out.println("待加密数组长度:" + byte_encode.length);
            byte[] byte_AES = cipher.doFinal(byte_encode);
            String AES_encode = new String(base64Encode(byte_AES));
            System.out.println("method1-加密：" + AES_encode);
            System.out.println("method1-加密后长度：" + AES_encode.length());
            /*
             * 解密
             */
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec("1111111111111111".getBytes()));//使用解密模式初始化 密钥
            byte[] byte_content = new BASE64Decoder().decodeBuffer(AES_encode);
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            System.out.println("method1-解密后：" + AES_decode);
            System.out.println("method1-解密后长度：" + AES_decode.length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    /**
     * 使用CFB_NOPADDING 算法 加密，默认模式
     */
    @Test
    public void method4() throws Exception {
//        String content = "这是一个测hi是啊1234324i！@#￥.%*()&$#=+-?<,/_&";
        String content = "222222222222222";
        System.out.println("原始数据:" + content);
        System.out.println("原始数据长度:" + content.length());
        try {
            /*
             * 加密
             */
            SecretKey key = getKey(encryptKey, (String) null);
            System.out.println("密钥的长度为：" + key.getEncoded().length);

//            Cipher cipher = Cipher.getInstance(ALGORITHM_CFB_PKCS5Padding);
            Cipher cipher = Cipher.getInstance(ALGORITHM_CFB_NOPADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(getIV())); //, new IvParameterSpec(getIV())
            byte[] byte_encode = content.getBytes("utf-8");
            System.out.println("待加密数组长度:" + byte_encode.length);
            byte[] byte_AES = cipher.doFinal(byte_encode);
            String AES_encode = new String(base64Encode(byte_AES));
            System.out.println("method1-加密：" + AES_encode);
            System.out.println("method1-加密后长度：" + AES_encode.length());
            /*
             * 解密
             */
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(getIV()));//使用解密模式初始化 密钥
            byte[] byte_content = new BASE64Decoder().decodeBuffer(AES_encode);
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            System.out.println("method1-解密后：" + AES_decode);
            System.out.println("method1-解密后长度：" + AES_decode.length());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void method5() throws Exception {
        String[] keys = {
                "这是一个测hi是啊1234324i!！@#￥.%*(  )&$#=+-?<,/_&"
                , "r"
                , "root"
                , "qwertyuiopasdfg"
                , "这是一个测试，呵呵。"
                , "222222222222222"
                , "22222222222222X"
                , "111111111111111111"
                , "11111111111111111X"
                , "18311119999"
                , "ksjdhflksadjfkajdkjfalkdhflakjdflakjdfhlakdhflkajdhflakdjflakdhfakjdfhlajdhflakjdhfalkjdfhaljkdhflaoiuvadf=-sdf"
        };
        for (String key : keys) {
            System.out.print(key + " | ");
            String encryptString = AESUtil.encode(key, "abc123");
            System.out.print(encryptString + " | ");
            String decryptString = AESUtil.decode(encryptString, "abc123");
            System.out.println(decryptString);
        }
    }


    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        if (isEmpty(content) || isEmpty(encryptKey)) {
            return null;
        }
        KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
        kgen.init(128, new SecureRandom(encryptKey.getBytes()));

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), KEY_ALGORITHM));

        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
        kgen.init(128, new SecureRandom(decryptKey.getBytes()));

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), KEY_ALGORITHM));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes);
    }

    /**
     * base 64 加密
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * base 64 解密
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception {
        return isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * From java lang3
     *
     * @param cs
     * @return
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * From java lang3
     *
     * @param cs
     * @return
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }
}
