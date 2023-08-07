/*

 */

package com.yami.shop.common.config;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.yami.shop.common.bean.AliDaYu;
import com.yami.shop.common.bean.ImgUpload;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lanhai
 */
@Configuration
@AllArgsConstructor
public class ShopBeanConfig {

	private final ShopBasicConfig shopBasicConfig;

    @Bean
    public AES tokenAes() {
        SecureUtil.disableBouncyCastle();
    	return new AES(shopBasicConfig.getTokenAesKey().getBytes());
    }

    @Bean
    public AliDaYu aLiDaYu () {
    	return shopBasicConfig.getALiDaYu();
    }

    @Bean
    public ImgUpload imgUpload() {
        return shopBasicConfig.getImgUpload();
    }
}
