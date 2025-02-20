package com.pc.pcparser.service;

import com.pc.pcparser.model.PcConfig;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelExporter {
    public void exportToExcelPcConfiguration(List<PcConfig> pcConfigList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("PC List");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "CPU", "Motherboard", "Memory",
                    "GPU", "SSD", "Power Supplier",
                    "Avg GPU Bench", "Gaming Score", "Prediction FPS FHD",
                    "Price per FPS", "Price", "Marker"};

            CellStyle headerStyle = getHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            CellStyle borderStyle = getBorderStyle(workbook);
            int rowNum = 1;
            for (PcConfig pcConfig : pcConfigList) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < headers.length; i++) {
                    row.createCell(i);
                }
                row.getCell(0).setCellValue(pcConfig.getId());
                row.getCell(1).setCellValue(pcConfig.getCpu().getName());
                row.getCell(2).setCellValue(pcConfig.getMotherboard().getManufacturer() + " "
                        + pcConfig.getMotherboard().getName());
                row.getCell(3).setCellValue(pcConfig.getMemory().getManufacturer() + " "
                        + pcConfig.getMemory().getName());
                row.getCell(4).setCellValue(pcConfig.getGpu().getManufacturer() + " "
                        + pcConfig.getGpu().getName() + " " + pcConfig.getGpu().getMemorySize());
                row.getCell(5).setCellValue(pcConfig.getSsd().getManufacturer() + " "
                        + pcConfig.getSsd().getName());
                row.getCell(6).setCellValue(pcConfig.getPowerSupplier().getManufacturer() + " "
                        + pcConfig.getPowerSupplier().getName() + " "
                        + pcConfig.getPowerSupplier().getPower() + "W");
                row.getCell(7).setCellValue(pcConfig.getAvgGpuBench());
                row.getCell(8).setCellValue(pcConfig.getGamingScore());
                row.getCell(9).setCellValue(pcConfig.getPredictionGpuFpsFhd());
                row.getCell(10).setCellValue(pcConfig.getPriceForFps());
                row.getCell(11).setCellValue(pcConfig.getPrice().doubleValue());
                if (pcConfig.getMarker() != null) {
                    row.getCell(12).setCellValue(pcConfig.getMarker().toString());
                }

                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(borderStyle);
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            sheet.createFreezePane(0, 1);
            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, headers.length - 1));

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private static CellStyle getBorderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}
