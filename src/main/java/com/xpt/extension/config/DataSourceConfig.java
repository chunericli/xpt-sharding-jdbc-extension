package com.xpt.extension.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.BindingTableRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.xpt.extension.strategy.DatabaseShardingAlgorithm;
import com.xpt.extension.strategy.TableShardingAlgorithm;

@Configuration
@MapperScan(basePackages = "com.xpt.extension.mapper", sqlSessionTemplateRef = "orderSqlSessionTemplate")
public class DataSourceConfig {

	@Bean(name = "order")
	@ConfigurationProperties(prefix = "spring.datasource.order")
	public DataSource order() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "extension")
	@ConfigurationProperties(prefix = "spring.datasource.extension")
	public DataSource extension() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * 当没有进行分库分表时，默认的数据源是order
	 * 
	 * @param order
	 * @param extension
	 * @return
	 */
	@Bean
	public DataSourceRule dataSourceRule(@Qualifier("order") DataSource order,
			@Qualifier("extension") DataSource extension) {
		Map<String, DataSource> dataSourceMap = new HashMap<>();
		dataSourceMap.put("order", order);
		dataSourceMap.put("extension", extension);
		return new DataSourceRule(dataSourceMap, "order");
	}

	@Bean
	public ShardingRule shardingRule(DataSourceRule dataSourceRule) {
		TableRule orderTableRule = TableRule.builder("t_order").actualTables(Arrays.asList("t_order_0", "t_order_1"))
				.tableShardingStrategy(new TableShardingStrategy("order_id", new TableShardingAlgorithm()))
				.dataSourceRule(dataSourceRule).build();

		List<BindingTableRule> bindingTableRules = new ArrayList<BindingTableRule>();
		bindingTableRules.add(new BindingTableRule(Arrays.asList(orderTableRule)));
		return ShardingRule.builder().dataSourceRule(dataSourceRule).tableRules(Arrays.asList(orderTableRule))
				.bindingTableRules(bindingTableRules)
				.databaseShardingStrategy(
						new DatabaseShardingStrategy("user_id", new DatabaseShardingAlgorithm()))
				.tableShardingStrategy(new TableShardingStrategy("order_id", new TableShardingAlgorithm()))
				.build();
	}

	/**
	 * 创建sharding-jdbc的数据源DataSource，MybatisAutoConfiguration会使用此数据源
	 * 
	 * @param shardingRule
	 * @return
	 * @throws SQLException
	 */
	@Bean(name = "dataSource")
	public DataSource shardingDataSource(ShardingRule shardingRule) throws SQLException {
		return ShardingDataSourceFactory.createDataSource(shardingRule);
	}

	@Bean
	public DataSourceTransactionManager transactitonManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "orderSqlSessionFactory")
	@Primary
	public SqlSessionFactory orderSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "orderSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate orderSqlSessionTemplate(
			@Qualifier("orderSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}