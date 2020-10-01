package com.chua.utils.tools.spider.process;

import com.chua.utils.tools.spider.config.ProcessConfiguration;
import com.chua.utils.tools.spider.config.SpiderConfig;
import com.chua.utils.tools.spider.interpreter.IPageInterpreter;
import net.sf.cglib.beans.BeanMap;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * WebMagic 进程
 *
 * @author CH
 */
public class WebMagicProcessor implements PageProcessor, IPageProcessor<Page> {

    private ProcessConfiguration processConfiguration;
    private SpiderConfig spiderConfig;
    private Set<IPageInterpreter> interpreters;

    @Override
    public void process(Page page) {
        if (null == interpreters || interpreters.size() == 0) {
            return;
        }
        for (IPageInterpreter interpreter : interpreters) {
            Set<String> modifier = interpreter.getModifier();
            Consumer<Map<String, List<String>>> consumer = interpreter.callback();
            consumer.accept(resolve(page, modifier));
        }
        page.addTargetRequests(page.getHtml().links().all());
    }

    /**
     * 页面解析
     * @param page 页面
     * @param modifiers 修饰符u
     * @return
     */
    private Map<String, List<String>> resolve(Page page, Set<String> modifiers) {
        Map<String, List<String>> result = new HashMap<>();
        for (String modifier : modifiers) {
            result.put(modifier, page.getHtml().xpath(modifier).all());
        }
        return result;
    }

    @Override
    public void config(ProcessConfiguration processConfiguration) {
        this.processConfiguration = processConfiguration;
        this.interpreters = this.processConfiguration.getInterpreters();
        this.spiderConfig = processConfiguration.getSpiderConfig();
    }

    @Override
    public Site getSite() {
        Site site = Site.me();
        BeanMap siteBeanMap = BeanMap.create(site);
        BeanMap spiderConfigBeanMap = BeanMap.create(spiderConfig);
        siteBeanMap.putAll(spiderConfigBeanMap);
        return (Site) siteBeanMap.getBean();
    }
}
