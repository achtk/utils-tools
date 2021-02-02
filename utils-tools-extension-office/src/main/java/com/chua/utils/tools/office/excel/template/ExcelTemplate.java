package com.chua.utils.tools.office.excel.template;

import com.chua.utils.tools.function.Template;
import com.chua.utils.tools.prop.placeholder.PropertiesPropertyPlaceholder;
import com.chua.utils.tools.prop.placeholder.PropertyPlaceholder;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于easyexcel - excel模板
 * <p>单个占位符{name}</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/30
 */
public class ExcelTemplate implements Template {
    @Override
    public String getTemplate(String template, Map<String, Object> params) {
        return null;
    }

    @Override
    public void writeAndClose(String template, String outPath, Map<String, Object>... params) {
        try {
            Files.copy(Paths.get(template), Paths.get(outPath), StandardCopyOption.REPLACE_EXISTING);
            // 读取源文件
            FileInputStream fis = new FileInputStream(outPath);
            HSSFWorkbook workBook = new HSSFWorkbook(fis);

            // 进行模板的克隆(接下来的操作都是针对克隆后的sheet)
            HSSFSheet sheet = workBook.cloneSheet(0);
            // 给sheet命名
            workBook.setSheetName(0, "sheet-0");
            //行数
            int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < physicalNumberOfRows; i++) {
                renderCell(sheet, i, params);
            }
        } catch (IOException e) {
        }

    }

    /**
     * @param sheet  sheet
     * @param index  索引
     * @param params 数据
     */
    private void renderCell(HSSFSheet sheet, int index, Map<String, Object>[] params) {
        HSSFRow sheetRow = sheet.getRow(index);
        int numberOfCells = sheetRow.getPhysicalNumberOfCells();
        int mapOrList = 0;
        List<String> item = new ArrayList<>();
        for (int j = 0; j < numberOfCells; j++) {
            HSSFCell sheetRowCell = sheetRow.getCell(j);
            String stringCellValue = sheetRowCell.getStringCellValue();
            item.add(stringCellValue);
            if (stringCellValue.startsWith("{{.") && stringCellValue.endsWith("}}")) {
                mapOrList = 2;
            } else if (stringCellValue.startsWith("{{") && stringCellValue.endsWith("}}")) {
                mapOrList = 1;
            }
        }

        if (mapOrList == 1) {
            PropertyPlaceholder propertyPlaceholder = new PropertiesPropertyPlaceholder();
            propertyPlaceholder.before("\\{\\{");
            propertyPlaceholder.after("\\}\\}");
            propertyPlaceholder.addPropertySource(params);
            for (int j = 0; j < item.size(); j++) {
                HSSFCell cell = sheetRow.getCell(j);
                String value = item.get(j);
                cell.setCellValue(propertyPlaceholder.placeholder(value, ""));
            }
        } else if (mapOrList == 2) {
            if (params.length < 1) {
                return;
            }
            PropertyPlaceholder propertyPlaceholder = new PropertiesPropertyPlaceholder();
            propertyPlaceholder.before("\\{\\{");
            propertyPlaceholder.after("\\}\\}");
            propertyPlaceholder.addPropertySource(params[0]);
            for (int j = 0; j < item.size(); j++) {
                HSSFCell cell = sheetRow.getCell(j);
                String value = item.get(j);
                cell.setCellValue(propertyPlaceholder.placeholder(value, ""));
            }

            for (int i = 1; i < params.length; i++) {
                Map<String, Object> param = params[i];
                PropertyPlaceholder propertyPlaceholder1 = new PropertiesPropertyPlaceholder();
                propertyPlaceholder1.before("\\{\\{");
                propertyPlaceholder1.after("\\}\\}");
                propertyPlaceholder1.addPropertySource(param);
                for (int j = 0; j < item.size(); j++) {
                    HSSFCell cell = sheetRow.getCell(j);
                    String value = item.get(j);
                    cell.setCellValue(propertyPlaceholder1.placeholder(value, ""));
                }
            }
        }
    }
}
