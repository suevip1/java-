package top.arhi.common.mybatis;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.*;

import java.util.Set;

public class GeoBaseInsertProvider extends MapperTemplate {

    public GeoBaseInsertProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insert(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        EntityColumn logicDeleteColumn = SqlHelper.getLogicDeleteColumn(entityClass);
        processKey(sql, entityClass, ms, columnList);
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            //忽略虚拟列
            if (column.getEntityField().isAnnotationPresent(VirtualGenerated.class)) {
                continue;
            }
            sql.append(column.getColumn() + ",");
        }
        sql.append("</trim>");
        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            //忽略虚拟列
            if (column.getEntityField().isAnnotationPresent(VirtualGenerated.class)) {
                continue;
            }
            if (logicDeleteColumn != null && logicDeleteColumn == column) {
                sql.append(SqlHelper.getLogicDeletedValue(column, false)).append(",");
                continue;
            }

            //优先使用传入的属性值,当原属性property!=null时，用原属性
            //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
            } else {
                if (column.getJavaType() == Geometry.class) {
                    //<if test="property != null">geomfromtext('point(108.9498710632 34.2588125935)'),</if>
                    sql.append(SqlHelper.getIfNotNull(column, getGeoColumnHolder(column), isNotEmpty()));
                } else {
                    //其他情况值仍然存在原property中
                    sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
                }

            }
            //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
            } else {
                if (column.getJavaType() == Geometry.class) {
                    //<if test="property == null">geomfromtext('point(108.9498710632 34.2588125935)'),</if>
                    sql.append(SqlHelper.getIfIsNull(column, getGeoColumnHolder(column), isNotEmpty()));
                } else {
                    //当null的时候，如果不指定jdbcType，oracle可能会报异常，指定VARCHAR不影响其他
                    sql.append(SqlHelper.getIfIsNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
                }
            }
        }
        sql.append("</trim>");
        return sql.toString();
    }

    public String insertSelective(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        //获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        EntityColumn logicDeleteColumn = SqlHelper.getLogicDeleteColumn(entityClass);
        processKey(sql, entityClass, ms, columnList);
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            //忽略虚拟列
            if (column.getEntityField().isAnnotationPresent(VirtualGenerated.class)) {
                continue;
            }
            if (column.isIdentity()) {
                sql.append(column.getColumn()).append(",");
            } else {
                if (logicDeleteColumn != null && logicDeleteColumn == column) {
                    sql.append(column.getColumn()).append(",");
                    continue;
                }
                sql.append(SqlHelper.getIfNotNull(column, column.getColumn() + ",", isNotEmpty()));
            }
        }
        sql.append("</trim>");

        sql.append("<trim prefix=\"VALUES(\" suffix=\")\" suffixOverrides=\",\">");
        for (EntityColumn column : columnList) {
            if (!column.isInsertable()) {
                continue;
            }
            //忽略虚拟列
            if (column.getEntityField().isAnnotationPresent(VirtualGenerated.class)) {
                continue;
            }
            if (logicDeleteColumn != null && logicDeleteColumn == column) {
                sql.append(SqlHelper.getLogicDeletedValue(column, false)).append(",");
                continue;
            }
            //优先使用传入的属性值,当原属性property!=null时，用原属性
            //自增的情况下,如果默认有值,就会备份到property_cache中,所以这里需要先判断备份的值是否存在
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheNotNull(column, column.getColumnHolder(null, "_cache", ",")));
            } else {
                if (column.getJavaType() == Geometry.class) {
                    //<if test="property != null">geomfromtext('point(108.9498710632 34.2588125935)'),</if>
                    sql.append(SqlHelper.getIfNotNull(column, getGeoColumnHolder(column), isNotEmpty()));
                } else {
                    //其他情况值仍然存在原property中
                    sql.append(SqlHelper.getIfNotNull(column, column.getColumnHolder(null, null, ","), isNotEmpty()));
                }
            }
            //当属性为null时，如果存在主键策略，会自动获取值，如果不存在，则使用null
            //序列的情况
            if (column.isIdentity()) {
                sql.append(SqlHelper.getIfCacheIsNull(column, column.getColumnHolder() + ","));
            }
        }
        sql.append("</trim>");
        return sql.toString();
    }

    private void processKey(StringBuilder sql, Class<?> entityClass, MappedStatement ms, Set<EntityColumn> columnList) {
        //Identity列只能有一个
        Boolean hasIdentityKey = false;
        //先处理cache或bind节点
        for (EntityColumn column : columnList) {
            if (column.isIdentity()) {
                //这种情况下,如果原先的字段有值,需要先缓存起来,否则就一定会使用自动增长
                //这是一个bind节点
                sql.append(SqlHelper.getBindCache(column));
                //如果是Identity列，就需要插入selectKey
                //如果已经存在Identity列，抛出异常
                if (hasIdentityKey) {
                    //jdbc类型只需要添加一次
                    if (column.getGenerator() != null && "JDBC".equals(column.getGenerator())) {
                        continue;
                    }
                    throw new MapperException(ms.getId() + "对应的实体类" + entityClass.getCanonicalName() + "中包含多个MySql的自动增长列,最多只能有一个!");
                }
                //插入selectKey
                SelectKeyHelper.newSelectKeyMappedStatement(ms, column, entityClass, isBEFORE(), getIDENTITY(column));
                hasIdentityKey = true;
            } else if (column.getGenIdClass() != null) {
                sql.append("<bind name=\"").append(column.getColumn()).append("GenIdBind\" value=\"@tk.mybatis.mapper.genid.GenIdUtil@genId(");
                sql.append("_parameter").append(", '").append(column.getProperty()).append("'");
                sql.append(", @").append(column.getGenIdClass().getCanonicalName()).append("@class");
                sql.append(", '").append(tableName(entityClass)).append("'");
                sql.append(", '").append(column.getColumn()).append("')");
                sql.append("\"/>");
            }

        }
    }

    /*
     * insert GEO字段占位符
     */
    private String getGeoColumnHolder(EntityColumn column) {
        return String.format("geomfromtext('point(${%s.longitude} ${%s.latitude})'),", column.getProperty(), column.getProperty());
    }

}
