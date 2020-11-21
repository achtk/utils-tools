package com.chua.tools.spider.process;

import com.chua.tools.spider.annotation.CssQuery;
import com.chua.tools.spider.config.CrawlerConf;
import com.chua.tools.spider.crawler.Crawler;
import com.chua.tools.spider.parser.PageParser;
import com.chua.tools.spider.parser.Parser;
import com.chua.tools.spider.request.PageRequest;
import com.chua.tools.spider.util.FieldReflectionUtil;
import com.chua.tools.spider.util.JsoupUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 页面解析器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Slf4j
public class PageParserProcessor implements ParserProcessor {

    private PageParser pageParser;
    private Crawler crawler;
    private CrawlerConf crawlerConf;
    private Document html;
    private Class<?> classType;

    @Override
    public boolean matcher(Parser parser) {
        if (parser instanceof PageParser) {
            this.pageParser = (PageParser) parser;
            return true;
        }
        return false;
    }

    @Override
    public boolean processor(Crawler crawler, PageRequest pageRequest) throws IOException {
        this.crawler = crawler;
        this.crawlerConf = crawler.getCrawlerConf();

        Document html = crawlerConf.getPageLoader().load(pageRequest);

        if (html == null) {
            return false;
        }
        this.html = html;
        // ------- child link list (FIFO队列,广度优先) ----------
        //limit child spread
        if (crawlerConf.isAllowSpread()) {
            Set<String> links = JsoupUtil.findLinks(html);
            if (links != null && links.size() > 0) {
                for (String item : links) {
                    //limit unvalid-child spread
                    if (crawlerConf.validWhiteUrl(item)) {
                        crawler.getUrlLoader().addUrl(item);
                    }
                }
            }
        }

        // ------- pagevo ----------
        // limit unvalid-page parse, only allow spread child, finish here
        if (!crawler.getCrawlerConf().validWhiteUrl(pageRequest.getUrl())) {
            return true;
        }

        return parseThePage();

    }

    /**
     * 解析页面
     *
     * @return
     */
    private boolean parseThePage() {
        // pagevo class-field info
        Class<?> annotationType = Object.class;

        Type pageVoParserClass = crawlerConf.getPageLoader().getClass().getGenericSuperclass();
        if (pageVoParserClass instanceof ParameterizedType) {
            Type[] pageVoClassTypes = ((ParameterizedType) pageVoParserClass).getActualTypeArguments();
            annotationType = (Class) pageVoClassTypes[0];
        }

        if (Object.class.isAssignableFrom(annotationType)) {
            pageParser.parse(html, null, null);
            return true;
        }

        CssQuery pageVoSelect = annotationType.getAnnotation(CssQuery.class);
        String value = (pageVoSelect != null && pageVoSelect.value() != null && pageVoSelect.value().trim().length() > 0) ? pageVoSelect.value() : "html";

        // pagevo document 2 object
        Elements pageVoElements = html.select(value);
        if (pageVoElements != null && pageVoElements.hasText()) {
            this.classType = annotationType;
            return parsePageNode(pageVoElements);
        }
        return true;
    }

    /**
     * 解析节点
     *
     * @param pageVoElements
     * @return
     */
    private boolean parsePageNode(Elements pageVoElements) {
        for (Element pageVoElement : pageVoElements) {
            Object pageVo = null;
            try {
                pageVo = classType.newInstance();
            } catch (Exception e) {
                return false;
            }
            Field[] fields = classType.getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    parseFieldAnnotations(pageVo, pageVoElement, field);
                }
            }

            // pagevo output
            pageParser.parse(html, pageVoElement, pageVo);
        }
        return true;
    }

    private void parseFieldAnnotations(Object pageVo, Element pageVoElement, Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            return;
        }

        // field origin value
        CssQuery fieldSelect = field.getAnnotation(CssQuery.class);
        String cssQuery = null;
        CssQuery.SelectType selectType = null;
        String selectVal = null;
        if (fieldSelect != null) {
            cssQuery = fieldSelect.value();
            selectType = fieldSelect.selectType();
            selectVal = fieldSelect.selectVal();
        }
        if (cssQuery == null || cssQuery.trim().length() == 0) {
            return;
        }

        // field value
        Object fieldValue = null;

        if (field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType fieldGenericType = (ParameterizedType) field.getGenericType();
            if (fieldGenericType.getRawType().equals(List.class)) {

                //Type gtATA = fieldGenericType.getActualTypeArguments()[0];
                Elements fieldElementList = pageVoElement.select(cssQuery);
                if (fieldElementList != null && fieldElementList.size() > 0) {

                    List<Object> fieldValueTmp = new ArrayList<Object>();
                    for (Element fieldElement : fieldElementList) {

                        String fieldElementOrigin = JsoupUtil.parseElement(fieldElement, selectType, selectVal);
                        if (fieldElementOrigin == null || fieldElementOrigin.length() == 0) {
                            continue;
                        }
                        try {
                            fieldValueTmp.add(FieldReflectionUtil.parseValue(field, fieldElementOrigin));
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }

                    if (fieldValueTmp.size() > 0) {
                        fieldValue = fieldValueTmp;
                    }
                }
            }
        } else {

            Elements fieldElements = pageVoElement.select(cssQuery);
            String fieldValueOrigin = null;
            if (fieldElements != null && fieldElements.size() > 0) {
                fieldValueOrigin = JsoupUtil.parseElement(fieldElements.get(0), selectType, selectVal);
            }

            if (fieldValueOrigin == null || fieldValueOrigin.length() == 0) {
                return;
            }

            try {
                fieldValue = FieldReflectionUtil.parseValue(field, fieldValueOrigin);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        if (fieldValue != null) {
            field.setAccessible(true);
            try {
                field.set(pageVo, fieldValue);
            } catch (IllegalAccessException e) {
            }
        }
    }
}
