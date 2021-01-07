package yuanxin.solr.generator.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;


import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yuanxin.solr.generator.entity.BuiltTableInfo;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.mapper.BuiltTableInfoMapper;
import yuanxin.solr.generator.mapper.TableInfoMapper;
import yuanxin.solr.generator.model.*;
import yuanxin.solr.generator.service.BuiltTableInfoService;
import yuanxin.solr.generator.service.DatabaseService;
import yuanxin.solr.generator.service.TableInfoService;

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
     * @param tableIdList 前端传入的数据 {@link List<Integer>}
     * @param built       是否构建的状态 {@link Boolean}
     * @return 是否成功 {@link Boolean}
     */
    @Override
    public boolean updateTableInfoBuiltStatus(List<Integer> tableIdList, boolean built) {
        boolean response = true;
        for (int tableId : tableIdList
        ) {
            TableInfo tableInfo = tableInfoService.selectById(tableId);
            tableInfo.setBuild(built);
            response = tableInfoService.updateById(tableInfo);
        }
        return response;
    }

    /**
     * 更新数据表table_info
     *
     * @param id   id {@link Integer}
     * @param save 是否构建的状态 {@link Boolean}
     * @return 是否成功 {@link Boolean}
     */
    @Override
    public boolean updateTableInfoSavedStatus(int id, boolean save) {
        TableInfo tableInfo = tableInfoService.selectById(id);
        if (tableInfo != null) {
            tableInfo.setSaved(save);
            return tableInfoService.updateById(tableInfo);
        }
        return false;
    }

    /**
     * 重置是否构建为否
     *
     * @return 是否成功 {@link Boolean}
     */
    @Override
    public boolean initTableBuild() {
        return tableInfoMapper.initTableBuild() > 0;
    }

    /**
     * 保存需要构建的字段
     *
     * @param saveInput 前端的传输的数据 {@link SaveInput}
     * @return 是否保存成功的信息 {@link SolrResult}
     */
    @Override
    public SolrResult saveGeneratorColumn(SaveInput saveInput) {
        int tableId = saveInput.getTableId();
        TableInfo tableInfo = tableInfoService.selectById(tableId);
        if (tableInfo != null) {
            List<String> inputColumnInfoList = saveInput.getColumnInfoList();
            String databaseName = tableInfo.getDatabaseName();
            String tableName = tableInfo.getTableName();
            // 获得数据库的信息
            List<BuiltTableInfo> builtTableInfoList = builtTableInfoMapper.getAllColumnInfoList(databaseName, tableName);
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
                saveList.add(
                        new BuiltTableInfo(
                                databaseName,
                                tableName,
                                columnInfo.getColumnName(),
                                columnInfo.getColumnType(),
                                columnInfo.getColumnInfo()
                        )
                );
            }
            boolean response = builtTableInfoService.insertBatch(saveList);
            updateTableInfoSavedStatus(tableId, true);
            return new SolrResult(response, "保存成功");
        }
        return new SolrResult(false, "保存成功");
    }

    /**
     * 通过表id获得表的字段信息
     *
     * @param getColumnInput 表对应的id {@link GetColumnInput}
     * @return 表的字段信息 {@link List<ColumnInfo>}
     */
    @Override
    public Page<ColumnInfo> getTableColumn(GetColumnInput getColumnInput) {
        // 获得全部字段
        int tableId = getColumnInput.getTableId();
        TableInfo tableInfo = tableInfoService.selectById(tableId);
        if (tableInfo != null) {
            Page page = PageDTO.buildPage(getColumnInput);
            List<BuiltTableInfo> allColumnList = builtTableInfoMapper.getColumnInfoPage(page, tableInfo.getDatabaseName(), tableInfo.getTableName());
            // 获得已构建的字段
            List<BuiltTableInfo> builtColumnList = getBuiltTableColumn(getColumnInput.getTableId());
            page.setRecords(getColumnHelper(allColumnList, builtColumnList));
            return page;
        }
        return null;
    }

    /**
     * 通过表id获得表的字段信息
     *
     * @param tableId 表对应的id {@link Integer}
     * @return 表的字段信息 {@link List<ColumnInfo>}
     */
    @Override
    public List<ColumnInfo> getAllTableColumn(int tableId) {

        TableInfo tableInfo = tableInfoService.selectById(tableId);
        if (tableInfo != null) {
            List<BuiltTableInfo> allColumnList = builtTableInfoMapper.getAllColumnInfoList(tableInfo.getDatabaseName(), tableInfo.getTableName());
            // 获得已构建的字段
            List<BuiltTableInfo> builtColumnList = getBuiltTableColumn(tableId);
            return getColumnHelper(allColumnList, builtColumnList);
        }
        return new ArrayList<>();
    }

    /**
     * 获得已经构建或者已经保存的表
     *
     * @param getTableInput 输入 {@link GetTableInput}
     * @return 已经构建或者已经保存的表 {@link Page<TableInfo>}
     */
    @Override
    public Page<TableInfo> getTableWithSavedStatus(GetTableInput getTableInput) {
        EntityWrapper<TableInfo> tableInfoQueryWrapper = new EntityWrapper<>();
        Page page = PageDTO.buildPage(getTableInput);
        tableInfoQueryWrapper.eq("saved", getTableInput.isSaved());
        if (getTableInput.getKey() != null) {
            tableInfoQueryWrapper.like("table_name", getTableInput.getKey()).or().like("database_name", getTableInput.getKey());
        }
        List<TableInfo> tableInfoList = tableInfoMapper.selectPage(page, tableInfoQueryWrapper);
        page = page.setRecords(tableInfoList);
        return page;
    }

    /**
     * 删除需要生成表的操作
     *
     * @param tableIdList 表对应的字段 {@link List<Integer>}
     * @return 是否删除成功的相关信息 {@link SolrResult}
     */
    @Override
    public SolrResult deleteBuiltOrSavedTable(List<Integer> tableIdList) {
        boolean response = true;
        for (int tableId : tableIdList
        ) {
            TableInfo tableInfo = tableInfoService.selectById(tableId);
            if (tableInfo != null) {
                tableInfo.setBuild(false);
                tableInfo.setSaved(false);
                tableInfoService.insertOrUpdate(tableInfo);
                EntityWrapper<BuiltTableInfo> builtTableInfoQueryWrapper = new EntityWrapper<>();
                builtTableInfoQueryWrapper
                        .eq("database_name", tableInfo.getDatabaseName())
                        .eq("table_name", tableInfo.getTableName());
                response = builtTableInfoService.delete(builtTableInfoQueryWrapper);
            }
        }
        if (!response) {
            return new SolrResult(false, "部分删除成功");
        } else {
            return new SolrResult(true, "删除成功");
        }
    }

    /**
     * 返回表数量 {@link Integer}
     *
     * @return 数量 {@link CountTableResult}
     */
    @Override
    public CountTableResult countTable() {
        EntityWrapper<TableInfo> buildQueryWrapper = new EntityWrapper<>();
        EntityWrapper<TableInfo> notBuildQueryWrapper = new EntityWrapper<>();
        buildQueryWrapper.eq("build", true).eq("saved", true);
        notBuildQueryWrapper.eq("build", false).eq("saved", true);
        return new CountTableResult(tableInfoMapper.selectCount(buildQueryWrapper), tableInfoMapper.selectCount(notBuildQueryWrapper));
    }

    /**
     * 表对应的id获得对应的表已生成的字段
     *
     * @param tableId 表对应的id {@link Integer}
     * @return 返回对应的表已生成的字段 {@link List<BuiltTableInfo>}
     */
    public List<BuiltTableInfo> getBuiltTableColumn(int tableId) {
        TableInfo tableInfo = tableInfoService.selectById(tableId);
        EntityWrapper<BuiltTableInfo> builtTableInfoQueryWrapper = new EntityWrapper<>();
        builtTableInfoQueryWrapper
                .eq("database_name", tableInfo.getDatabaseName())
                .eq("table_name", tableInfo.getTableName());
        return builtTableInfoService.selectList(builtTableInfoQueryWrapper);
    }


    /**
     * 获得已经标注了是否已经构建的字段信息
     *
     * @param allColumnList   全部字段 {@link List<BuiltTableInfo>}
     * @param builtColumnList 已经构建的字段{@link List<BuiltTableInfo>}
     * @return 标注好是否已经构建的字段信息 {@link List<ColumnInfo>}
     */
    private List<ColumnInfo> getColumnHelper(List<BuiltTableInfo> allColumnList, List<BuiltTableInfo> builtColumnList) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        if (builtColumnList != null) {
            for (BuiltTableInfo allColumn : allColumnList
            ) {
                boolean flag = false;
                for (BuiltTableInfo builtColumn : builtColumnList
                ) {
                    if (allColumn.getColumnName().equals(builtColumn.getColumnName())) {
                        columnInfoList.add(new ColumnInfo(allColumn.getColumnName(), allColumn.getColumnType(), allColumn.getColumnInfo(), true));
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    columnInfoList.add(new ColumnInfo(allColumn.getColumnName(), allColumn.getColumnType(), allColumn.getColumnInfo(), false));
                }
            }
        }
        return columnInfoList;
    }
}
