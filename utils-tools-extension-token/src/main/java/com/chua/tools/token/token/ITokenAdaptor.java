package com.chua.tools.token.token;

import com.chua.tools.token.entity.TokenConfig;

import java.util.function.Predicate;

/**
 * token
 * @author CH
 * @since 1.0
 */
public interface ITokenAdaptor {
    /**
     * 生成token
     * @param tokenConfig  token配置
     * @exception Exception 创建失败
     * @return
     */
    public String token(TokenConfig tokenConfig) throws Exception;

    /**
     * 生成token
     * @exception Exception 创建失败
     * @return
     */
    default public String token() throws Exception {
        return token(new TokenConfig(10 * 60 * 1000));
    }

    /**
     * 生成token
     * @param expire 到期时间(ms)
     * @exception Exception 创建失败
     * @return
     */
    default public String token(long expire) throws Exception {
        return token(new TokenConfig(expire));
    }
    /**
     * 校验
     * @return
     */
    public boolean verify(String token, Predicate predicate) ;
}
