package com.chua.utils.tools.stream;

import com.chua.utils.tools.common.JsonHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * json stream
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/21
 */
public class JsonStream {

    private final JsonNode jsonNode;
    private JsonNode jsonCopyNode;

    public JsonStream(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
        this.reset();
    }

    public static JsonStream of(String jsonStr) throws IOException {
        return new JsonStream(JsonHelper.OBJECT_MAPPER.readTree(jsonStr));
    }

    /**
     * 查找节点
     *
     * @param path 路径
     * @return this
     */
    public JsonStream findPath(final String path) {
        this.jsonCopyNode = jsonCopyNode.findPath(path);
        return this;
    }

    /**
     * 查找节点
     *
     * @param index 索引
     * @return this
     */
    public JsonStream findPath(final int index) {
        this.jsonCopyNode = jsonCopyNode.path(index);
        return this;
    }

    /**
     * 查找节点
     *
     * @param index 索引
     * @param value 值
     * @return this
     */
    public JsonStream findPath(final String index, final Object value) {
        List<JsonNode> jsonNodeList = new ArrayList<>();
        jsonCopyNode.elements().forEachRemaining(jsonNode1 -> {
            if (jsonNode1.has(index)) {
                if (jsonNode1.findValue(index).asText().equals(value.toString())) {
                    jsonNodeList.add(jsonNode1);
                }
            }
        });

        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.withExactBigDecimals(false), jsonNodeList);
        this.jsonCopyNode = arrayNode;
        return this;
    }

    /**
     * 查找节点
     *
     * @param path 路径
     * @return this
     */
    public JsonStream findValue(final String path) {
        this.jsonCopyNode = jsonCopyNode.findValue(path);
        return this;
    }

    /**
     * 重置
     *
     * @return
     */
    public JsonStream reset() {
        this.jsonCopyNode = jsonNode.deepCopy();
        return this;
    }

    /**
     * 获取数据
     *
     * @return this
     */
    public String asText() {
        return jsonCopyNode.asText();
    }

    /**
     * 获取数据
     *
     * @param defaultValue 默认值
     * @return this
     */
    public String asText(final String defaultValue) {
        return jsonCopyNode.asText(defaultValue);
    }

    /**
     * 获取数据
     *
     * @return this
     */
    public Boolean asBoolean() {
        return jsonCopyNode.asBoolean();
    }

    /**
     * 获取数据
     *
     * @param defaultValue 默认值
     * @return this
     */
    public Boolean asBoolean(final Boolean defaultValue) {
        return jsonCopyNode.asBoolean(defaultValue);
    }

    /**
     * 获取数据
     *
     * @return this
     */
    public Double asDouble() {
        return jsonCopyNode.asDouble();
    }

    /**
     * 获取数据
     *
     * @param defaultValue 默认值
     * @return this
     */
    public Double asDouble(final Double defaultValue) {
        return jsonCopyNode.asDouble(defaultValue);
    }

    /**
     * 获取数据
     *
     * @return this
     */
    public int asInt() {
        return jsonCopyNode.asInt();
    }

    /**
     * 获取数据
     *
     * @param defaultValue 默认值
     * @return this
     */
    public Long asLong(final int defaultValue) {
        return jsonCopyNode.asLong(defaultValue);
    }

    /**
     * 获取数据
     *
     * @return this
     */
    public byte[] binaryValue() throws IOException {
        return jsonCopyNode.binaryValue();
    }
}
