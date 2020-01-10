package com.mall.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description: 动态验证码缓存
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/22 10:45
 */
public class OtpCache {
    private final static Logger logger = Logger.getLogger(ItemCache.class);

    /**
     * Jedis连接池
     */
    private JedisPool jedisPool;

    private OtpCache(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    /**
     * 通过字节码及字节码对应对象的属性，将字节码数据传递给这些属性，实现序列化
     */
    private RuntimeSchema<String> schema = RuntimeSchema.createFrom(String.class);

    /**
     * 获取otpCode
     * @param phone 手机号
     * @return String
     */
    public String getOtpCode(String phone){
        try (Jedis jedis = jedisPool.getResource();){
            String key = "phone:" + phone;
            //引入protoStuff，采用自定义序列化
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null){
                String otpCode = schema.newMessage();
                //ProtoStuff根据字节数组存放进数据，按照scheam将数据传到空对象中
                ProtostuffIOUtil.mergeFrom(bytes,otpCode,schema);
                //反序列化
                return otpCode;
            }
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }
        return null;
    }

    /**
     * 放入otpCode
     * @param phone 手机号
     * @param otpCode 动态验证码
     * @return String
     */
    public String putOtpCode(String phone,String otpCode){
        try (Jedis jedis = jedisPool.getResource();){
            String key = "phone:" + phone;
            byte[] bytes = ProtostuffIOUtil.toByteArray(otpCode,schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            //设置超时缓存时间1分钟
            int timeout = 60;
            return jedis.setex(key.getBytes(),timeout,bytes);
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }
        return null;
    }
}
