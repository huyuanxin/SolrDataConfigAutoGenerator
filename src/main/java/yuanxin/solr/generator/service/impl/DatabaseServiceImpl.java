package yuanxin.solr.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import yuanxin.solr.generator.entity.BuiltTableInfo;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.mapper.BuiltTableInfoMapper;
import yuanxin.solr.generator.mapper.TableInfoMapper;
import yuanxin.solr.generator.model.ColumnInfo;
import yuanxin.solr.generator.model.GeneratorInput;
import yuanxin.solr.generator.model.SaveInput;
import yuanxin.solr.generator.service.DatabaseService;
import yuanxin.solr.generator.service.BuiltTableInfoService;
import yuanxin.solr.generator.service.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 复杂数据库操作的实现
 *
 * @author huyuanxin
 */
@Service
public class DatabaseServiceImpl implements DatabaseService {
    final TableInfoMapper tableInfoMapper;
    final BuiltTableInfoMapper builtTableInfoMapper;
    final BuiltTableInfoService builtTableInfoService;
    final TableInfoService tableInfoService;

    @Autowired
    public DatabaseServiceImpl(TableInfoMapper tableInfoMapper, BuiltTableInfoMapper builtTableInfoMapper, BuiltTableInfoService builtTableInfoService, TableInfoService tableInfoService) {
        this.tableInfoMapper = tableInfoMapper;
        this.builtTableInfoMapper = builtTableInfoMapper;
        this.builtTableInfoService = builtTableInfoService;
        this.tableInfoService = tableInfoService;
    }

    /**
     * 更新数据表table_info
     *
     * @param generatorInput 前端传入的数据
     * @param built          是否构建的状态
     * @return 成功的条数
     */
    @Override
    public boolean updateTableInfoBuiltStatus(GeneratorInput generatorInput, boolean built) {
        boolean response = true;
        List<Integer> tableIdList = generatorInput.getTableIdList();
        for (int tableId : tableIdList
        ) {
            TableInfo tableInfo = tableInfoService.getById(tableId);
            tableInfo.setBuild(built);
            response = tableInfoService.updateById(tableInfo);
        }
        return response;
    }

    /**
     * 更新数据表table_info
     *
     * @param id   id
     * @param save 是否构建的状态
     * @return 成功的条数
     */
    @Override
    public boolean updateTableInfoSavedStatus(int id, boolean save) {
        TableInfo tableInfo = tableInfoService.getById(id);
        if (tableInfo != null) {
            tableInfo.setSaved(save);
            return tableInfoService.updateById(tableInfo);
        }
        return true;
    }

    /**
     * 重置是否构建为否
     *
     * @return 是否成功
     */
    @Override
    public int initTableBuild() {
        return tableInfoMapper.initTableBuild();
    }

    /**
     * 保存需要构建的字段
     *
     * @param saveInput 前端的传输的数据
     * @return 保存条数
     */
    @Override
    public boolean saveGeneratorColumn(SaveInput saveInput) {
        int tableId = saveInput.getTableId();
        TableInfo tableInfo = tableInfoService.getById(tableId);
        if (tableInfo != null) {
            List<String> inputColumnInfoList = saveInput.getColumnInfoList();
            String databaseName = tableInfo.getDatabaseName();
            String tableName = tableInfo.getTableName();
            // 获得数据库的信息
            List<BuiltTableInfo> builtTableInfoList = builtTableInfoMapper.getColumnInfoList(databaseName, tableName);
            List<BuiltTableInfo> columnInfoList = new ArrayList<>();
            for (String s : inputColumnInfoList
            ) {
                for (BuiltTableInfo info : builtTableInfoList
                ) {
                    if (info.getColumnName().equals(s)) {
                        columnInfoList.add(info);
                        break;
                    }
                }
            }
            // 清除数据库
            Map<String, Object> columnMap = new HashMap<>(2);
            columnMap.put("database_name", tableInfo.getDatabaseName());
            columnMap.put("table_name", tableInfo.getTableName());
            builtTableInfoMapper.deleteByMap(columnMap);
            // 保存到数据库
            List<BuiltTableInfo> saveList = new ArrayList<>();
            for (BuiltTableInfo columnInfo : columnInfoList
            ) {
                saveList.add(new BuiltTableInfo(
                                databaseName,
                                tableName,
                                columnInfo.getColumnName(),
                                columnInfo.getColumnType(),
                                columnInfo.getColumnInfo()
                        )
                );
            }
            boolean response = builtTableInfoService.saveBatch(saveList);
            updateTableInfoSavedStatus(tableId, true);
            return response;
        }
        return false;
    }

    /**
     * 通过表id获得表的字段信息
     *
     * @param tableId 表对应的id
     * @return 表的字段信息
     */
    @Override
    public List<ColumnInfo> getTableColumn(int tableId) {
        // 获得全部字段
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        TableInfo tableInfo = tableInfoService.getById(tableId);
        if (tableInfo != null) {
            List<BuiltTableInfo> allColumnList = builtTableInfoMapper.getColumnInfoList(tableInfo.getDatabaseName(), tableInfo.getTableName());
            // 获得已构建的字段
            List<BuiltTableInfo> builtColumnList = getBuiltTableColumn(tableId);
            if (builtColumnList != null) {
                // 获得未构建的字段
                for (BuiltTableInfo list : builtColumnList
                ) {
                    allColumnList.removeIf(it -> it.getColumnName().equals(list.getColumnName()));
                }

                for (BuiltTableInfo builtTableInfo : builtColumnList
                ) {
                    columnInfoList.add(new ColumnInfo(
                            builtTableInfo.getColumnName(),
                            builtTableInfo.getColumnType(),
                            builtTableInfo.getColumnInfo(), true)
                    );
                }
            }
            for (BuiltTableInfo builtTableInfo : allColumnList
            ) {
                columnInfoList.add(new ColumnInfo(
                        builtTableInfo.getColumnName(),
                        builtTableInfo.getColumnType(),
                        builtTableInfo.getColumnInfo(), false)
                );
            }
            return columnInfoList;
        }
        return new ArrayList<>();
    }

    @Override
    public List<TableInfo> getBuiltOrSavedTable() {
        QueryWrapper<TableInfo> tableInfoQueryWrapper = new QueryWrapper<>();
        tableInfoQueryWrapper.eq("saved", true);
        return tableInfoService.list(tableInfoQueryWrapper);
    }

    /**
     * 删除需要生成表的操作
     *
     * @param tableIdList 表对应的字段
     * @return 是否删除成功
     */
    @Override
    public boolean deleteBuiltOrSavedTable(List<Integer> tableIdList) {
        boolean response = true;
        for (int tableId : tableIdList
        ) {
            TableInfo tableInfo = tableInfoService.getById(tableId);
            if (tableInfo != null) {
                tableInfo.setBuild(false);
                tableInfo.setSaved(false);
                tableInfoService.saveOrUpdate(tableInfo);
                QueryWrapper<BuiltTableInfo> builtTableInfoQueryWrapper = new QueryWrapper<>();
                builtTableInfoQueryWrapper
                        .eq("database_name", tableInfo.getDatabaseName())
                        .eq("table_name", tableInfo.getTableName());
                response = builtTableInfoService.remove(builtTableInfoQueryWrapper);
            } else {
                response = false;
            }
        }
        return response;
    }


    public List<BuiltTableInfo> getBuiltTableColumn(int tableId) {
        TableInfo tableInfo = tableInfoService.getById(tableId);
        QueryWrapper<BuiltTableInfo> builtTableInfoQueryWrapper = new QueryWrapper<>();
        builtTableInfoQueryWrapper
                .eq("database_name", tableInfo.getDatabaseName())
                .eq("table_name", tableInfo.getTableName());
        return builtTableInfoService.list(builtTableInfoQueryWrapper);
    }
}
