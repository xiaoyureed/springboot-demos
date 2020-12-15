package io.github.xiaoyureed.restapiscaffold.respapimavenplugin.impl;

import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.GlobalConfig;
import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.ICodeGenerator;
import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.util.DatabaseUtils;
import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.util.PathUtils;
import io.github.xiaoyureed.restapiscaffold.respapimavenplugin.util.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaoyu
 * date: 2020/7/27
 */
public class VelocityCodeGeneratorImpl implements ICodeGenerator {

    private static final VelocityEngine engine;

    private static final VelocityContext context;

    private static final GlobalConfig config = GlobalConfig.me();

    /**
     * 数据库原始字段信息
     * key - column name
     * value - full class name
     */
    private static final Map<String, String> FIELDS_RAW = DatabaseUtils.getColumns(config);

    static {
        engine =  new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
        engine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        engine.init();

        context = new VelocityContext();
        context.put("packageName", config.basePackage);
        context.put("domain", config.domainName);
        context.put("domainWithUppercase", StringUtils.uppercaseFirstLetter(config.domainName));
        context.put("now", LocalDate.now().toString());

        context.put("mapperLocations", config.mapperLocations);

        // 用于生成 mapper 中的 SQL 字段
        //key - 字段名, value - field名
        final Map<String, String> columnFieldMap = new LinkedHashMap<>();
        FIELDS_RAW.keySet().stream()
                .filter(s -> !s.equalsIgnoreCase("id") // 排除id字段
                        && !s.equalsIgnoreCase("is_del")) // 排除 is_del
                .collect(Collectors.toList())
                .forEach(column -> columnFieldMap.put(column, StringUtils.convertToCamel(column)));
        context.put("columnFieldMap", columnFieldMap);
        context.put("table", config.tableName);

        // 用于生成 imports
        final List<String> fullTypeNames = FIELDS_RAW.values().stream()
                // 过滤掉 string 和 long 类型, 因为无需显式导入
                .filter(s -> !s.contains("String") && !s.contains("Long"))
                .distinct().collect(Collectors.toList());
        context.put("fullTypeNames", new HashSet<>(fullTypeNames));

        // 用于生成 fields
        // key 为字段名, value 为 类型
        final HashMap<String, String> columnTypeMap = new HashMap<>(10);
        FIELDS_RAW.forEach((columnName, fullTypeName) -> {
            if (!columnName.equalsIgnoreCase("is_del")) {
                columnTypeMap.put(StringUtils.convertToCamel(columnName), StringUtils.getNameFromFullName(fullTypeName));
            }
        });
        context.put("columnTypeMap", columnTypeMap);
    }

    @Override
    public void generate(Log log) {
        log.info("db columns: " + FIELDS_RAW);

        common();
        dto();
        mapper();
        service();
        controller();
    }

    private void common() {
        writeWithFullName("template/common/aop/GlobalExceptionHandler.vm", PathUtils.commonAopPath(), "GlobalExceptionHandler.java");

        writeWithFullName("template/common/config/DataSourceConfig.vm", PathUtils.commonConfigPath(), "DataSourceConfig.java");
        writeWithFullName("template/common/config/HibernateValidatorConfig.vm", PathUtils.commonConfigPath(), "HibernateValidatorConfig.java");
        writeWithFullName("template/common/config/WebMvcConfig.vm", PathUtils.commonConfigPath(), "WebMvcConfig.java");

        writeWithFullName("template/common/dao/ReadDao.vm", PathUtils.commonDaoPath(), "ReadDao.java");
        writeWithFullName("template/common/dao/WriteDao.vm", PathUtils.commonDaoPath(), "WriteDao.java");
        writeWithFullName("template/common/dao/impl/mybatis/ReadDaoMybatisImpl.vm", PathUtils.commonDaoImplMybatis(), "ReadDaoMybatisImpl.java");
        writeWithFullName("template/common/dao/impl/mybatis/WriteDaoMybatisImpl.vm", PathUtils.commonDaoImplMybatis(), "WriteDaoMybatisImpl.java");

        writeWithFullName("template/common/exception/BizException.vm", PathUtils.commonExceptionPath(), "BizException.java");

        writeWithFullName("template/common/interceptor/PagingInterceptor.vm", PathUtils.commonInterceptor(), "PagingInterceptor.java");

        writeWithFullName("template/common/util/BeanUtils.vm", PathUtils.commonUtil(), "BeanUtils.java");
        writeWithFullName("template/common/util/FileUtils.vm", PathUtils.commonUtil(), "FileUtils.java");
        writeWithFullName("template/common/util/StringUtils.vm", PathUtils.commonUtil(), "StringUtils.java");

        writeWithFullName("template/common/BaseResp.vm", PathUtils.commonPath(), "BaseResp.java");
        writeWithFullName("template/common/Constants.vm", PathUtils.commonPath(), "Constants.java");
        writeWithFullName("template/common/PagingInfo.vm", PathUtils.commonPath(), "PagingInfo.java");
        writeWithFullName("template/common/Service.vm", PathUtils.commonPath(), "Service.java");
        writeWithFullName("template/common/ThreadLocalContext.vm", PathUtils.commonPath(), "ThreadLocalContext.java");
    }

    private void dto() {
        // f01 related dto
        writeWithTailName("template/pojo/dto/F01Req.vm", PathUtils.pojoDtoPath(), "F01ReqM01.java");
        writeWithTailName("template/pojo/dto/F01Resp.vm", PathUtils.pojoDtoPath(), "F01RespM01.java");
        writeWithTailName("template/sql/dto/F01Input.vm", PathUtils.sqlDtoPath(), "F01InputM01.java");

        // f02
        writeWithTailName("template/pojo/dto/F02Req.vm", PathUtils.pojoDtoPath(), "F02ReqM01.java");
        writeWithTailName("template/pojo/dto/F02Resp.vm", PathUtils.pojoDtoPath(), "F02RespM01.java");
        writeWithTailName("template/sql/dto/F02Input.vm", PathUtils.sqlDtoPath(), "F02InputM01.java");

        // f03 related dto
        writeWithTailName("template/pojo/dto/F03Req.vm", PathUtils.pojoDtoPath(), "F03ReqM01.java");
        writeWithTailName("template/pojo/dto/F03Resp.vm", PathUtils.pojoDtoPath(), "F03RespM01.java");
        writeWithTailName("template/sql/dto/F03Input.vm", PathUtils.sqlDtoPath(), "F03InputM01.java");

        // f04 related dto
        writeWithTailName("template/pojo/dto/F04Req.vm", PathUtils.pojoDtoPath(), "F04ReqM01.java");
        writeWithTailName("template/pojo/dto/F04RespM.vm", PathUtils.pojoDtoPath(), "F04RespM01.java");
        writeWithTailName("template/pojo/dto/F04RespS.vm", PathUtils.pojoDtoPath(), "F04RespS01.java");
        writeWithTailName("template/sql/dto/F04Input.vm", PathUtils.sqlDtoPath(), "F04InputM01.java");
        writeWithTailName("template/sql/dto/F04Output.vm", PathUtils.sqlDtoPath(), "F04OutputM01.java");
    }

    private void mapper() {
        writeWithTailName("template/mapper/F01Mapper.vm", PathUtils.mapperPath(), "F01Mapper.xml");
        writeWithTailName("template/mapper/F02Mapper.vm", PathUtils.mapperPath(), "F02Mapper.xml");
        writeWithTailName("template/mapper/F03Mapper.vm", PathUtils.mapperPath(), "F03Mapper.xml");
        writeWithTailName("template/mapper/F04Mapper.vm", PathUtils.mapperPath(), "F04Mapper.xml");
    }

    private void service() {
        writeWithTailName("template/service/F01Service.vm", PathUtils.servicePath(), "F01Service.java");
        writeWithTailName("template/service/F02Service.vm", PathUtils.servicePath(), "F02Service.java");
        writeWithTailName("template/service/F03Service.vm", PathUtils.servicePath(), "F03Service.java");
        writeWithTailName("template/service/F04Service.vm", PathUtils.servicePath(), "F04Service.java");

    }

    private void controller() {
        this.writeWithTailName("template/controller/Controller.vm", PathUtils.controllerPath(), "Controller.java");
    }

    private void writeWithFullName(String tplLocation, String dir, String fullName) {
        write(tplLocation, dir, fullName, false);
    }

    private void write(String tplLocation, String dir, String fileName, boolean isTail) {
        final StringWriter sw = new StringWriter();
        FileWriter fileWriter = null;

        engine.getTemplate(tplLocation).merge(context, sw);
        final String fileLocation = isTail? (dir + StringUtils.uppercaseFirstLetter(config.domainName) + fileName):(dir + fileName);
        try {
            File file = new File(fileLocation);
            // 如果父文件夹不存在, 则创建
            //todo
            File parentFile = file.getParentFile();
            while (!parentFile.exists()) {
                boolean mkdirs = parentFile.mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException(">>> Error of create file: " + fileLocation);
                }
                parentFile = parentFile.getParentFile();
            }
            fileWriter = new FileWriter(fileLocation);
            fileWriter.write(sw.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void writeWithTailName(String tplLocation, String dir, String tailName) {
        write(tplLocation, dir, tailName, true);

    }

    @Override
    public void clear() {

    }

    @Override
    public void clear(String domain) {

    }
}
