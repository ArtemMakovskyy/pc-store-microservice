package com.pc.pcparser.service;

import com.pc.pcparser.dto.PcConfigDto;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelExporter {

    public void exportToExcelPcConfiguration(List<PcConfigDto> pcConfigList, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("PC List");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Part Number", "CPU", "CPU URL", "Motherboard", "Motherboard URL", "Memory", "Memory URL",
                    "GPU", "GPU URL", "SSD", "SSD URL", "Power Supplier", "Power Supplier URL",
                    "Price", "Prediction FPS", "Gaming Score", "Price per FPS", "Marker"};

            CellStyle headerStyle = getHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            CellStyle borderStyle = getBorderStyle(workbook);
            CellStyle lightGreenStyle = getLightGreenStyle(workbook);
            CellStyle whiteStyle = getWhiteStyle(workbook);
            CellStyle lightBlueStyle = getLightBlueStyle(workbook);

            int rowNum = 1;
            for (PcConfigDto pcConfig : pcConfigList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pcConfig.getPartNumber());
                row.createCell(1).setCellValue(pcConfig.getCpu());
                row.createCell(2).setCellValue(pcConfig.getCpuUrl());
                row.createCell(3).setCellValue(pcConfig.getMotherboard());
                row.createCell(4).setCellValue(pcConfig.getMotherboardUrl());
                row.createCell(5).setCellValue(pcConfig.getMemory());
                row.createCell(6).setCellValue(pcConfig.getMemoryUrl());
                row.createCell(7).setCellValue(pcConfig.getGpu());
                row.createCell(8).setCellValue(pcConfig.getGpuUrl());
                row.createCell(9).setCellValue(pcConfig.getSsd());
                row.createCell(10).setCellValue(pcConfig.getSsdUrl());
                row.createCell(11).setCellValue(pcConfig.getPowerSupplier());
                row.createCell(12).setCellValue(pcConfig.getPowerSupplierUrl());
                row.createCell(13).setCellValue(pcConfig.getPrice().doubleValue());
                row.createCell(14).setCellValue(pcConfig.getPredictionFps());
                row.createCell(15).setCellValue(pcConfig.getGamingScore());
                row.createCell(16).setCellValue(pcConfig.getPriceForFps());
                row.createCell(17).setCellValue(pcConfig.getMarker());

                CellStyle rowStyle = (pcConfig.getMarker() != null && pcConfig.getMarker().equals("BEST_PRICE")) ? lightBlueStyle : (rowNum % 2 == 0 ? lightGreenStyle : whiteStyle);

                for (int i = 0; i < headers.length; i++) {
                    Cell cell = row.getCell(i);
                    cell.setCellStyle(borderStyle);
                    addBorders(cell, rowStyle);
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            setUrlColumnWidth(sheet);
            setDataColumnWidth(sheet);

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

    private static CellStyle getLightGreenStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private static CellStyle getWhiteStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private static CellStyle getLightBlueStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private void setUrlColumnWidth(Sheet sheet){
        sheet.setColumnWidth(2, 5 * 256);
        sheet.setColumnWidth(4, 5 * 256);
        sheet.setColumnWidth(6, 5 * 256);
        sheet.setColumnWidth(8, 5 * 256);
        sheet.setColumnWidth(10, 5 * 256);
        sheet.setColumnWidth(12, 5 * 256);
    }

    private void setDataColumnWidth(Sheet sheet){
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(5, 30 * 256);
        sheet.setColumnWidth(7, 35 * 256);
        sheet.setColumnWidth(9, 20 * 256);
        sheet.setColumnWidth(11, 30 * 256);
    }

    private void addBorders(Cell cell, CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        cell.setCellStyle(style);
    }
}
