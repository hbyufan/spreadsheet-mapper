package me.excel.tools.setter;

import me.excel.tools.FieldUtils;
import me.excel.tools.importer.ExcelImportException;
import me.excel.tools.model.excel.ExcelCell;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static me.excel.tools.FieldUtils.getFieldWithoutPrefix;


/**
 * default object value setter
 *
 * Created by hanwen on 15-12-18.
 */
public class DefaultValueSetter {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultValueSetter.class);

  static {
    ConvertUtils.register(new DateConverter(null), java.util.Date.class);
    ConvertUtils.register(new CalendarConverter(null), Calendar.class);
    ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
    ConvertUtils.register(new SqlTimeConverter(null), Time.class);
    ConvertUtils.register(new SqlTimestampConverter(null), Timestamp.class);
    ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
  }

  public void set(Object data, List<ExcelCell> excelCells) {

    excelCells.forEach(cell -> {
      try {
        BeanUtils.setProperty(data, getFieldWithoutPrefix(cell.getField()), matches(data, cell) ? cell.getValue() : null);
      } catch (Exception e) {
        LOGGER.error(ExceptionUtils.getStackTrace(e));
        throw new ExcelImportException(e);
      }
    });

  }

  private boolean matches(Object data, ExcelCell cell) {
    Class fieldType = FieldUtils.getFieldType(data.getClass(), getFieldWithoutPrefix(cell.getField()).split("\\."));

    if (Integer.class.isAssignableFrom(fieldType) || int.class.isAssignableFrom(fieldType) ||
        Long.class.isAssignableFrom(fieldType) || long.class.isAssignableFrom(fieldType) ||
        Double.class.isAssignableFrom(fieldType) || double.class.isAssignableFrom(fieldType) ||
        Float.class.isAssignableFrom(fieldType) || float.class.isAssignableFrom(fieldType) ||
        String.class.isAssignableFrom(fieldType)) {
      return true;
    }

    return false;
  }
}
