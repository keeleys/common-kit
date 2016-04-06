package com.ttianjun.common.kit.excel;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ttianjun.common.kit.Lists;

public class PoiExporter {

    public static final String VERSION_2003 = "2003";
    private static final int HEADER_ROW = 1;
    private static final int MAX_ROWS = 65535;
    private String data_format ="yyyy-MM-dd HH:mm:ss";
    
    private String version;
    private String[] sheetNames = new String[]{"sheet"};
    private int cellWidth = 8000;
    private int headerRow;
    private String[][] headers;
    private String[][] columns;
    
    private List<?>[] data;
    
    public PoiExporter dataFormat(String data_format) {
    	this.data_format = data_format;
    	return this;
	}

	public PoiExporter(List<?>... data) {
        this.data = data;
        this.column(new String[]{});
    }

    public static PoiExporter data(List<?>... data) {
        return new PoiExporter(data);
    }

    public static List<List<?>> dice(List<?> num, int chunkSize) {
        int size = num.size();
        int chunk_num = size / chunkSize + (size % chunkSize == 0 ? 0 : 1);
        List<List<?>> result = Lists.newArrayList();
        for (int i = 0; i < chunk_num; i++) {
            result.add(num.subList(i * chunkSize, i == chunk_num - 1 ? size : (i + 1) * chunkSize));
        }
        return result;
    }

    public Workbook export() throws Exception {
        Workbook wb;
        if (VERSION_2003.equals(version)) {
            wb = new HSSFWorkbook();
            if (data.length > 1) {
                for (int i = 0; i < data.length; i++) {
                    List<?> item = data[i];
                }
            } else if (data.length == 1 && data[0].size() > MAX_ROWS) {
                data = dice(data[0], MAX_ROWS).toArray(new List<?>[]{});
                String sheetName = sheetNames[0];
                sheetNames = new String[data.length];
                for (int i = 0; i < data.length; i++) {
                    sheetNames[i] = sheetName + (i == 0 ? "" : (i + 1));
                }
                String[] header = headers[0];
                headers = new String[data.length][];
                for (int i = 0; i < data.length; i++) {
                    headers[i] = header;
                }
                String[] column = columns[0];
                columns = new String[data.length][];
                for (int i = 0; i < data.length; i++) {
                    columns[i] = column;
                }
            }
        } else {
            wb = new XSSFWorkbook();
        }
        if (data.length == 0) {
            return wb;
        }
        for (int i = 0; i < data.length; i++) {
            Sheet sheet = wb.createSheet(sheetNames[i]);
            Row row;
            Cell cell;
            if (headers[i].length > 0) {
                row = sheet.createRow(0);
                if (headerRow <= 0) {
                    headerRow = HEADER_ROW;
                }
                headerRow = Math.min(headerRow, MAX_ROWS);
                for (int h = 0, lenH = headers[i].length; h < lenH; h++) {
                    if (cellWidth > 0) {
                        sheet.setColumnWidth(h, cellWidth);
                    }
                    cell = row.createCell(h);
                    cell.setCellValue(headers[i][h]);
                }
            }

            for (int j = 0, len = data[i].size(); j < len; j++) {
                row = sheet.createRow(j + headerRow);
                Object obj = data[i].get(j);
                if (obj == null) {
                    continue;
                }
                if (obj instanceof Map) {
                    processAsMap(columns[i], row, obj);
                }  else {
                    processAsObject(columns[i], row, obj);
                } 
            }
        }
        return wb;
    }

    private void processAsMap(String[] columns, Row row, Object obj) {
        Cell cell;
        Map<String, Object> map = (Map<String, Object>) obj;
        if (columns.length == 0) { // show all if column not specified
            Set<String> keys = map.keySet();
            int columnIndex = 0;
            for (String key : keys) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(map.get(key) == null ? "" : map.get(key) + "");
                columnIndex++;
            }
        } else {
            for (int j = 0, len = columns.length; j < len; j++) {
                cell = row.createCell(j);
                cell.setCellValue(map.get(columns[j]) == null ? "" : map.get(columns[j]) + "");
            }
        }
    }

    private void processAsObject(String[] columns, Row row, Object obj) throws Exception{
    	Cell cell;
    	if (columns.length == 0) { // show all if column not specified
            int columnIndex = 0;
            for(Field field :obj.getClass().getDeclaredFields()){
            	cell = row.createCell(columnIndex);
                cell.setCellValue(field == null ? "" : getFieldValue(field,obj) + "");
        		columnIndex++;
        	}
        } else {
            for (int j = 0, len = columns.length; j < len; j++) {
            	Field field =obj.getClass().getDeclaredField(columns[j]);
                cell = row.createCell(j);
                cell.setCellValue(field == null ? "" : getFieldValue(field,obj) + "");
            }
        }
    	
    }
    private String getFieldValue(Field field,Object obj) throws IllegalArgumentException, IllegalAccessException{
    	field.setAccessible(true);
    	Object value = field.get(obj);
    	if(field.getType().getName().equals("java.util.Date"))
        	value = new SimpleDateFormat(data_format).format((java.util.Date)value);
		return value+"";
    }

    public PoiExporter version(String version) {
        this.version = version;
        return this;
    }

    public PoiExporter sheetName(String sheetName) {
        this.sheetNames = new String[]{sheetName};
        return this;
    }

    public PoiExporter sheetNames(String... sheetName) {
        this.sheetNames = sheetName;
        return this;
    }

    public PoiExporter cellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    public PoiExporter headerRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public PoiExporter header(String... header) {
        this.headers = new String[][]{header};
        return this;
    }

    public PoiExporter headers(String[]... headers) {
        this.headers = headers;
        return this;
    }

    public PoiExporter column(String... column) {
        this.columns = new String[][]{column};
        return this;
    }

    public PoiExporter columns(String[]... columns) {
        this.columns = columns;
        return this;
    }

}
