package com.chua.utils.netx.etxd.context;

import com.chua.utils.netx.function.NetStream;
import com.chua.utils.netx.function.RKv;
import com.chua.utils.netx.function.RKvProducer;
import com.chua.utils.tools.common.ByteHelper;
import com.chua.utils.tools.exceptions.NotSupportedException;
import com.chua.utils.tools.properties.NetProperties;
import com.google.common.base.Strings;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * etcd context
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public class JetcdContext implements RKvProducer<String, String>, AutoCloseable {

    private RKv<String, String> rKv = new SimpleRkv();
    /**
     * etcl客户端链接
     */
    @Getter
    private Client client = null;

    public JetcdContext(NetProperties netProperties) {
        ClientBuilder builder = Client.builder();
        if (!Strings.isNullOrEmpty(netProperties.getUsername())) {
            builder.user(ByteSequence.from(netProperties.getUsername(), UTF_8));
        }

        if (!Strings.isNullOrEmpty(netProperties.getPassword())) {
            builder.password(ByteSequence.from(netProperties.getPassword(), UTF_8));
        }
        builder.connectTimeoutMs(netProperties.getConnectionTimeout());

        client = builder.endpoints(netProperties.getHost()).build();
    }


    @Override
    public void close() throws Exception {
        if (null != client) {
            client.close();
        }
    }

    @Override
    public RKv<String, String> getKv() {
        return rKv;
    }

    /**
     *
     */
    final class SimpleRkv implements RKv<String, String>, NetStream {

        @Override
        public void set(String key, String value) throws Exception {
            client.getKVClient().put(getByteValue(key), getByteValue(value));
        }

        @Override
        public String get(String key) throws Exception {
            GetResponse getResponse = client.getKVClient().get(getByteValue(key), GetOption.newBuilder().build()).get();
            // key does not exist
            if (getResponse.getKvs().isEmpty()) {
                return null;
            }

            return getResponse.getKvs().get(0).getValue().toString(UTF_8);
        }

        @Override
        public void incr(String key, int step, int ttl) throws Exception {
            CompletableFuture<GetResponse> completableFuture = client.getKVClient().get(getByteValue(key), GetOption.newBuilder().build());
            GetResponse response = null;
            if (ttl < 0) {
                response = completableFuture.get();
            } else {
                response = completableFuture.get(ttl, TimeUnit.SECONDS);
            }
            if (response.getKvs().isEmpty()) {
                throw new NullPointerException();
            }
            ByteSequence byteSequence = response.getKvs().get(0).getValue();
            byte[] bytes = byteSequence.getBytes();
            int anInt = 0;
            try {
                anInt = ByteHelper.toInt(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            anInt += 1;
            set(key, String.valueOf(anInt));
        }

        @Override
        public void incr(String key, int step) throws Exception {
            incr(key, step, -1);
        }

        @Override
        public void set(Map<String, String> map) throws Exception {
            if (null == map) {
                throw new NullPointerException();
            }

            map.entrySet().forEach(entry -> {
                try {
                    set(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void set(Map<String, String> map, int ttl) throws Exception {
            set(map);
        }

        @Override
        public void set(String key, String value, int ttl) throws Exception {
            client.getKVClient().put(getByteValue(key), getByteValue(value), PutOption.newBuilder().build());
        }

        @Override
        public void ttl(String key, int ttl) throws Exception {
            throw new NotSupportedException();
        }

        @Override
        public Map<String, String> get(List<String> keys) throws Exception {
            Map<String, String> values = new HashMap<>(keys.size());
            keys.forEach(key -> {
                try {
                    String s = get(key);
                    values.put(key, s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return values;
        }

        @Override
        public void del(String key) throws Exception {
            client.getKVClient().delete(getByteValue(key)).get();
        }

        @Override
        public void delPrefix(String keyPrefix) throws Exception {
            client.getKVClient().delete(getByteValue(keyPrefix), DeleteOption.newBuilder().withPrefix(getByteValue(keyPrefix)).build());
        }

        @Override
        public List<String> keys(String keyPrefix) throws Exception {
            GetResponse response = client.getKVClient().get(getByteValue(keyPrefix), GetOption.newBuilder().withPrefix(getByteValue(keyPrefix)).build()).get();
            if (response.getKvs().isEmpty()) {
                return Collections.emptyList();
            }
            return response.getKvs().stream().map(keyValue -> keyValue.getValue().toString(UTF_8)).collect(Collectors.toList());
        }

        @Override
        public List<String> getKeys(String keyPrefix) throws Exception {
            GetResponse response = client.getKVClient().get(getByteValue(keyPrefix), GetOption.newBuilder().withPrefix(getByteValue(keyPrefix)).build()).get();
            if (response.getKvs().isEmpty()) {
                return Collections.emptyList();
            }
            return response.getKvs().stream().map(keyValue -> keyValue.getKey().toString(UTF_8)).collect(Collectors.toList());
        }

        public ByteSequence getByteValue(String value) {
            return ByteSequence.from(value, UTF_8);
        }

        @Override
        public Stream stream() {
            KV kvClient = client.getKVClient();
            return Stream.of(kvClient);
        }
    }
}
