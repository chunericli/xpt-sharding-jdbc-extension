package com.xpt.extension.strategy;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

public class DatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Long> {

	@Override
	public String doEqualSharding(Collection<String> databaseNames, ShardingValue<Long> shardingValue) {
		if (Long.parseLong(shardingValue.getValue().toString()) % 2 == 0) {
			return "order";
		} else {
			return "extension";
		}
	}

	@Override
	public Collection<String> doInSharding(Collection<String> databaseNames, ShardingValue<Long> shardingValue) {
		Collection<String> result = new LinkedHashSet<>(databaseNames.size());
		for (Long value : shardingValue.getValues()) {
			for (String tableName : databaseNames) {
				if (tableName.endsWith(value % 2 + "")) {
					result.add(tableName);
				}
			}
		}
		return result;
	}

	@Override
	public Collection<String> doBetweenSharding(Collection<String> databaseNames, ShardingValue<Long> shardingValue) {
		Collection<String> result = new LinkedHashSet<>(databaseNames.size());
		Range<Long> range = (Range<Long>) shardingValue.getValueRange();
		for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
			for (String each : databaseNames) {
				if (each.endsWith(i % 2 + "")) {
					result.add(each);
				}
			}
		}
		return result;
	}
}