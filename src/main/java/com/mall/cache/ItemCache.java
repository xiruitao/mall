package com.mall.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.mall.entity.Item;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description: 商品Redis缓存
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/8/21 16:21
 */
public class ItemCache {
    private final static Logger logger = Logger.getLogger(ItemCache.class);

    /**
     * Jedis连接池
     */
    private JedisPool jedisPool;

    private ItemCache(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }

    /**
     * 通过字节码及字节码对应对象的属性，将字节码数据传递给这些属性，实现序列化
     */
    private RuntimeSchema<Item> schema = RuntimeSchema.createFrom(Item.class);

    /**
     * 取商品缓存
     * @param itemId 商品ID
     * @return Item
     */
    public Item getItem(Integer itemId){
        try (Jedis jedis = jedisPool.getResource();){
            String key = "item:"+itemId;
            //引入protoStuff，采用自定义序列化
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null){
                Item item = schema.newMessage();
                //ProtoStuff根据字节数组存放进数据，按照scheam将数据传到空对象中
                ProtostuffIOUtil.mergeFrom(bytes,item,schema);
                //item 被反序列化
                return item;
            }
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }
        return null;
    }

    /**
     * 存商品缓存
     * @param item 商品实体
     * @return String
     */
    public String putItem(Item item){
        try (Jedis jedis = jedisPool.getResource();){
            String key = "item:"+item.getItemId();
            byte[] bytes = ProtostuffIOUtil.toByteArray(item,schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            //设置超时缓存时间30分钟
            int timeout = 30 * 60;
            return jedis.setex(key.getBytes(),timeout,bytes);
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }
        return null;
    }

    /**
     * 更新商品缓存
     * @param itemId 商品ID key
     */
    public void updateItem(String itemId,Item item){
        try (Jedis jedis = jedisPool.getResource();){
            String key = "item:"+itemId;
            byte[] bytes = ProtostuffIOUtil.toByteArray(item,schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            jedis.set(key.getBytes(),bytes);
        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }
    }
}
