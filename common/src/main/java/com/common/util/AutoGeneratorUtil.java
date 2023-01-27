package com.common.util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

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
import com.common.entity.AutoGeneratorEntity;

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
		help.append(tip + ": ");
		System.out.println(help.toString());

		return scanner.nextLine();
	}

	/**
	 * RUN THIS
	 */
	public static void main(String[] args) {
		init();
	}

	public static void init(AutoGeneratorEntity autoGeneratorEntity) {
		genCode(autoGeneratorEntity);
	}

	public static void init() {
		String url = scanner("[1] 请输入数据库地址 (127.0.0.1:3306)");
		String username = scanner("[2] 请输入数据库用户名");
		String password = scanner("[3] 请输入数据库地址");
		String database = scanner("[4] 请输入数据库名 (必须有)");
		String tableName = scanner("[5] 请输入表名 (多个表名使用逗号分隔)");
		String moduleName = scanner("[6] 请输入模块名");
		String packageName = scanner(
				String.format("[7] 请输入包名%s", moduleName.isEmpty() ? "" : String.format(" (%s)", moduleName)));
		String author = scanner(String.format("[8] 请输入作者 (izilong)"));

		AutoGeneratorEntity autoGeneratorEntity = new AutoGeneratorEntity();

		if (packageName.isEmpty()) {
			packageName = moduleName;
		}

		if (!url.isEmpty()) {
			autoGeneratorEntity.setUrl(url);
		}

		if (!username.isEmpty()) {
			autoGeneratorEntity.setUsername(username);
		}

		if (!password.isEmpty()) {
			autoGeneratorEntity.setPassword(password);
		}

		if (!author.isEmpty()) {
			autoGeneratorEntity.setAuthor(author);
		}

		autoGeneratorEntity.setDatabse(database)
				.setPackageName(packageName)
				.setTableName(tableName)
				.setModuleName(moduleName);

		genCode(autoGeneratorEntity);
	}

	public static void genCode(AutoGeneratorEntity autoGeneratorEntity) {
		String moduleName = autoGeneratorEntity.getModuleName();
		String database = autoGeneratorEntity.getDatabse();
		String packageName = autoGeneratorEntity.getPackageName();
		boolean isRootModule = moduleName.isEmpty();
		String modulePath = isRootModule ? "" : "/" + moduleName;

		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		String outputDir = String.format("%s%s/src/main/java", projectPath, modulePath);
		gc.setOutputDir(outputDir);
		gc.setAuthor(autoGeneratorEntity.getAuthor());
		gc.setOpen(false);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		boolean isRootDataBase = database.isEmpty();
		String url = String.format(
				"jdbc:mysql://%s%s?useUnicode=true&useSSL=false&characterEncoding=utf8",
				autoGeneratorEntity.getUrl(),
				isRootDataBase ? "" : "/" + database);
		dsc.setUrl(url);
		// dsc.setSchemaName("public");
		dsc.setDriverName(autoGeneratorEntity.getDriverName());
		dsc.setUsername(autoGeneratorEntity.getUsername());
		dsc.setPassword(autoGeneratorEntity.getPassword());
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName(isRootModule ? null : packageName);
		pc.setParent(autoGeneratorEntity.getParentName());
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};
		List<FileOutConfig> focList = new ArrayList<>();
		String[] tableNames = getTableName(database, autoGeneratorEntity.getTableName(), dsc);

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

		long start = System.currentTimeMillis();
		log.debug("[START] 准备生成文件...");
		mpg.execute();
		log.debug(String.format("[END] 文件生成完成, 共耗时[%s]ms !", System.currentTimeMillis() - start));
	}

	private static String[] getTableName(String database, String scannerTable, DataSourceConfig dsc) {
		if (!scannerTable.isEmpty()) {
			return scannerTable.split(",");
		}

		ArrayList<String> list = new ArrayList<>();
		boolean empty = database.isEmpty();

		try {
			ResultSet catalogs = dsc.getConn().getMetaData().getTables(empty ? null : database, null, null,
					new String[] { "TABLE" });

			while (catalogs.next()) {
				String name = catalogs.getString("TABLE_NAME");
				list.add(name);
			}

			log.debug("[扫描] [{}]数据库下共有[{}]张表: {}", empty ? "" : database, list.size(),
					list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list.toArray(new String[] {});
	}

}
