package com.lovezhima.boot.core.builder;

/**
 * 生成编码code
 *
 * @author king
 * @since 2023.1
 */
public interface CodeBuildProvider {

    /**
     * 生成编码code
     * @param config 编码配置
     * @return 返回编码
     */
    String generateCode(CodeBuildConfig config);
}
