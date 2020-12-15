package io.github.xiaoyureed.restapiscaffold.respapimavenplugin;

import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.impl.VelocityCodeGeneratorImpl;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 每一个 Mojo 就是 Maven 中的一个执行目标（executable goal）
 * @author xiaoyu
 * date: 2020/7/21
 */
// 在处理源码的时候，plugin-tools 会把使用了 @Mojo 注解
// 或 Javadoc 里包含 @goal 注释的类来当作一个 Mojo 类.
// @Mojo(name = "generate", defaultPhase = LifecyclePhase.CLEAN)
@Mojo(name = "generate")
public class RestApiMojo extends AbstractMojo {

    private final Log log = getLog();

    @Parameter(property = "mapperLocations", defaultValue = "mybatis.mapper-locations")
    private String mapperLocations;

    @Parameter(property = "basePackage")
    private String basePackage;

    @Parameter(property = "jdbcUrl")
    private String jdbcUrl;
    @Parameter(property = "driverClassName")
    private String driverClassName;
    @Parameter(property = "username")
    private String username;
    @Parameter(property = "password")
    private String password;

    @Parameter(property = "domainName")
    private String domainName;
    @Parameter(property = "tableName")
    private String tableName;

    public void execute() throws MojoExecutionException, MojoFailureException {
        generate();
    }

    private void generate() {
        log.info(">>> code generate ...");

        initConfig();
        ICodeGenerator generator = new VelocityCodeGeneratorImpl();
        generator.generate(log);
    }

    private void initConfig() {
        GlobalConfig result = GlobalConfig.me();

        result.mapperLocations = mapperLocations;
        result.basePackage = basePackage;
        result.jdbcUrl = jdbcUrl;
        result.driverClassName = driverClassName;
        result.username = username;
        result.password = password;
        result.domainName = domainName;
        result.tableName = tableName;

        result.selfCheck(log);
    }
}
