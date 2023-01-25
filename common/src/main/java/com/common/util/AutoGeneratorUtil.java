package com.common.util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * mysql 代码生成器演示例子
 * </p>
 */
@Slf4j
public class AutoGeneratorUtil {

	private static final class FileOutConfigExtension extends FileOutConfig {
		private final String projectPath;
		private final String modulePath;

		private FileOutConfigExtension(String templatePath, String projectPath, String modulePath) {
			super(templatePath);
			this.projectPath = projectPath;
			this.modulePath = modulePath;
		}

		@Override
		public String outputFile(TableInfo tableInfo) {
			// 自定义输入文件名称
			String name = String.format(
					"%s%s/src/main/resources/mapper/%sMapper%s",
					projectPath,
					modulePath,
					tableInfo.getEntityName(),
					StringPool.DOT_XML);
			return name;
		}
	}

	/**
	 * <p>
	 * 读取控制台内容
	 * </p>
	 */
	private static String scanner(String tip) {
		Scanner scanner = new Scanner(System.in);
		StringBuilder help = new StringBuilder();
		help.append("请输入" + tip + "：");
		System.out.println(help.toString());
		if (scanner.hasNext()) {
			String ipt = scanner.next();
			if (StringUtils.hasLength(tip)) {
				return ipt;
			}
		}

		throw new MybatisPlusException("请输入正确的" + tip + "！");
	}

	/**
	 * RUN THIS
	 */
	public static void main(String[] args) {
		init();
		// genCode("test", "test", "dept_log");
	}

	public static void init() {
		String scannerModule = scanner("模块名");
		String scannerDatabase = scanner("数据库名");
		String scannerTable = scanner("表名 (多个表名使用逗号分隔，*为所有表)");

		genCode(scannerDatabase, scannerModule, scannerTable);
	}

	public static void init(String database) {
		String scannerModule = scanner("模块名");
		String scannerTable = scanner("表名 (多个表名使用逗号分隔)");

		genCode(database, scannerModule, scannerTable);
	}

	public static void genCode(String scannerDatabase, String scannerModule, String scannerTable) {
		boolean isRootModule = Objects.equals("/", scannerModule);
		String modulePath = isRootModule ? "" : "/" + scannerModule;

		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		String outputDir = String.format("%s%s/src/main/java", projectPath, modulePath);
		gc.setOutputDir(outputDir);
		gc.setAuthor("izilong");
		gc.setOpen(false);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		boolean isRootDataBase = Objects.equals("/", scannerDatabase);
		String url = String.format(
				"jdbc:mysql://127.0.0.1:3306%s?useUnicode=true&useSSL=false&characterEncoding=utf8",
				isRootDataBase ? "" : "/" + scannerDatabase);
		dsc.setUrl(url);
		// dsc.setSchemaName("public");
		dsc.setDriverName("com.mysql.cj.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("root");
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName(scannerModule);
		pc.setParent("com");
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};
		List<FileOutConfig> focList = new ArrayList<>();
		String[] tableNames = getTableName(isRootDataBase ? null : scannerDatabase, scannerTable, dsc);

		Stream.of(tableNames).forEach(
				(o) -> focList.add(new FileOutConfigExtension("/templates/mapper.xml.ftl", projectPath, modulePath)));

		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);
		mpg.setTemplate(new TemplateConfig().setXml(null));

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		// strategy.setSuperEntityClass("com.baomidou.mybatisplus.samples.generator.common.BaseEntity");
		strategy.setEntityLombokModel(true);
		// strategy.setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController");
		strategy.setInclude(tableNames);
		strategy.setSuperEntityColumns("id");
		strategy.setControllerMappingHyphenStyle(true);
		strategy.setTablePrefix(pc.getModuleName() + "_");
		mpg.setStrategy(strategy);
		// 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());
		log.debug("[START] 准备生成文件...");
		mpg.execute();
		log.debug("[END] 文件生成完成!!!");
	}

	private static String[] getTableName(String scannerDatabase, String scannerTable, DataSourceConfig dsc) {
		if (!Objects.equals("*", scannerTable)) {
			return scannerTable.split(", |,");
		}

		ArrayList<String> list = new ArrayList<>();

		try {
			ResultSet catalogs = dsc.getConn().getMetaData().getTables(scannerDatabase, null, null,
					new String[] { "TABLE" });

			while (catalogs.next()) {
				String name = catalogs.getString("TABLE_NAME");
				list.add(name);
			}

			log.debug("扫描[{}]数据库下共有[{}]张表: {}", Objects.isNull(scannerDatabase) ? "/" : scannerDatabase, list.size(),
					list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list.toArray(new String[] {});
	}

}
