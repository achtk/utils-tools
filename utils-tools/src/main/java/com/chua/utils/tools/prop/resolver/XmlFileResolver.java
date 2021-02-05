package com.chua.utils.tools.prop.resolver;

import com.chua.utils.tools.constant.SuffixConstant;
import com.chua.utils.tools.function.FileConverter;
import com.chua.utils.tools.function.NoneFileConverter;
import com.chua.utils.tools.prop.mapper.FileMapper;
import com.google.common.collect.HashMultimap;
import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 * yaml解析
 *
 * @author CH
 */
public class XmlFileResolver implements IFileResolver {

    private HashMultimap<Object, Object> hashMultimap = HashMultimap.create();

    @Override
    public void stream(InputStream inputStream) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setIgnoringComments(true);
        try {
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setEntityResolver(new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    if (systemId.endsWith(SuffixConstant.DTD)) {
                        return new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>".getBytes()));
                    }
                    return null;
                }
            });


            Document document = documentBuilder.parse(inputStream);
            Element element = document.getDocumentElement();
            checkAttribute("", element.getAttributes());
            checkElement("", element.getChildNodes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析节点
     *
     * @param prefix     前缀
     * @param childNodes 节点
     */
    private void checkElement(String prefix, NodeList childNodes) {
        int length = childNodes.getLength();
        if (length == 1) {
            Node node = childNodes.item(0);
            hashMultimap.put(prefix + "." + node.getNodeName(), node.getNodeValue());
            return;
        }
        String newPrefix;
        for (int i = 0; i < length; i++) {
            Node node = childNodes.item(i);
            newPrefix = prefix + "." + node.getNodeName();
            checkAttribute(newPrefix, node.getAttributes());
            checkElement(newPrefix, node.getChildNodes());
        }
    }

    /**
     * 解析属性
     *
     * @param prefix     前缀
     * @param attributes 属性
     */
    private void checkAttribute(String prefix, NamedNodeMap attributes) {
        int length = attributes.getLength();
        if (length == 0) {
            return;
        }

        for (int i = 0; i < length; i++) {
            Node node = attributes.item(i);
            hashMultimap.put(prefix + "." + node.getNodeName(), node.getNodeValue());
        }
    }

    @Override
    public FileMapper analysis(FileConverter fileConverter) {
        if (null == hashMultimap) {
            return null;
        }
        if (null == fileConverter) {
            fileConverter = new NoneFileConverter();
        }
        HashMultimap<Object, Object> multimap = HashMultimap.create();
        FileMapper fileMapper = new FileMapper();
        fileMapper.setHashMultimap(multimap);

        Set<Map.Entry<Object, Object>> entries = this.hashMultimap.entries();
        for (Map.Entry<Object, Object> entry : entries) {
            multimap.put(entry.getKey(), fileConverter.doBackward(entry.getValue()));
        }
        return fileMapper;
    }

    @Override
    public String[] suffixes() {
        return new String[]{"properties"};
    }
}
