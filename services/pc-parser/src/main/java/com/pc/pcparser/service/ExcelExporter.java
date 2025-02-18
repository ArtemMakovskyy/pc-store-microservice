package com.pc.pcparser.service;

import com.pc.pcparser.model.Pc;
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
    public void exportToExcelPcConfiguration(List<Pc> pcList, String filePath) {
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
            for (Pc pc : pcList) {
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < headers.length; i++) {
                    row.createCell(i);
                }
                row.getCell(0).setCellValue(pc.getId());
                row.getCell(1).setCellValue(pc.getCpu().getName());
                row.getCell(2).setCellValue(pc.getMotherboard().getManufacturer() + " "
                        + pc.getMotherboard().getName());
                row.getCell(3).setCellValue(pc.getMemory().getManufacturer() + " "
                        + pc.getMemory().getName());
                row.getCell(4).setCellValue(pc.getGpu().getManufacturer() + " "
                        + pc.getGpu().getName() + " " + pc.getGpu().getMemorySize());
                row.getCell(5).setCellValue(pc.getSsd().getManufacturer() + " "
                        + pc.getSsd().getName());
                row.getCell(6).setCellValue(pc.getPowerSupplier().getManufacturer() + " "
                        + pc.getPowerSupplier().getName() + " "
                        + pc.getPowerSupplier().getPower() + "W");
                row.getCell(7).setCellValue(pc.getAvgGpuBench());
                row.getCell(8).setCellValue(pc.getGamingScore());
                row.getCell(9).setCellValue(pc.getPredictionGpuFpsFhd());
                row.getCell(10).setCellValue(pc.getPriceForFps());
                row.getCell(11).setCellValue(pc.getPrice().doubleValue());
                if (pc.getMarker() != null) {
                    row.getCell(12).setCellValue(pc.getMarker().toString());
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
