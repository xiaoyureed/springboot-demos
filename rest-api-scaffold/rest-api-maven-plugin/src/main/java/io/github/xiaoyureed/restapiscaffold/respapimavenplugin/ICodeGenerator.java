package io.github.xiaoyureed.restapiscaffold.respapimavenplugin;

import org.apache.maven.plugin.logging.Log;

/**
 * @author xiaoyu
 * date: 2020/7/27
 */
public interface ICodeGenerator {
    void generate(Log log);

    void clear();

    void clear(String domain);
}
