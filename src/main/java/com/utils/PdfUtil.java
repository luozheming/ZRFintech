package com.utils;

import com.dto.indto.PdfDto;
import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * pdf文件相关工具类
 */
public class PdfUtil {

    // 定义全局的字体静态变量
    private static Font titleFont;
    private static Font headFont;
    private static Font textFont;
    private static Font nullTextFont;
    // 最大宽度
    private static int maxWidth = 520;
    // 静态代码块
    static {
        try {
            // 不同字体（这里定义为同一种字体：包含不同字号、不同style）
            BaseFont bfChinese = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont, AsianFontMapper.ChineseSimplifiedEncoding_H, BaseFont.NOT_EMBEDDED);
            titleFont = new Font(bfChinese, 16, Font.BOLD);
            headFont = new Font(bfChinese, 12, Font.BOLD);
            textFont = new Font(bfChinese, 12, Font.NORMAL);
            nullTextFont = new Font(bfChinese, 12, Font.ITALIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成pdf文件
     * @param pdfDtoList
     */
    public static ByteArrayOutputStream createPdf(List<PdfDto> pdfDtoList) throws Exception {
        String title = pdfDtoList.get(0).getTitle();
        // 1.新建document对象
        Document document = new Document(PageSize.A4);// 建立一个Document对象

        // 2.建立一个书写器(Writer)与document对象关联
//        File file = new File(String.valueOf(paramMap.get("filePath")));
//        File file = new File("D://test.pdf");
//        file.createNewFile();
//        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

        // 2:建立一个PDF 写入器与document对象关联通过书写器(Writer)可以将文档写入到磁盘中
        StringBuilder fileName = new StringBuilder();
        fileName.append(title).append(".pdf");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        // 3.打开文档
        document.open();

        // 4:向文档添加内容
        // 标题
        Paragraph titleParagraph = new Paragraph(title, titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);// 居中
        titleParagraph.setSpacingAfter(20f); //设置段落下空白
        document.add(titleParagraph);

        // 正文
        Paragraph paragraph = null;
        int n = 1;
        for (PdfDto pdfDto : pdfDtoList) {
//            paragraph = new Paragraph(n + "、" + pdfDto.getHead(), headFont);
//            paragraph.setAlignment(Element.ALIGN_LEFT);// 设置文字居中 0靠左 1，居中 2，靠右
//            paragraph.setIndentationLeft(10); //设置左缩进
//            paragraph.setLeading(20f); //行间距
//            paragraph.setSpacingAfter(5f); //设置段落下空白
//            document.add(paragraph);// 正文添加小标题

            String text = StringUtils.isEmpty(pdfDto.getText()) ? "无" : pdfDto.getText();
            Font textFonts = StringUtils.isEmpty(pdfDto.getText()) ? nullTextFont : textFont;
            paragraph = new Paragraph(text, textFonts);
            paragraph.setAlignment(Element.ALIGN_LEFT);// 设置文字居中 0靠左 1，居中 2，靠右
            paragraph.setIndentationLeft(30); //设置左缩进
            paragraph.setLeading(20f); //行间距
            paragraph.setSpacingAfter(20f); //设置段落下空白
            document.add(paragraph);// 正文添加小标题下的文本内容
            n ++;
        }

        // 5.关闭文档
        document.close();

        return baos;
    }

    private static void generatePDF(Document document) throws Exception {
        // 段落
        Paragraph paragraph = new Paragraph("路演稿", titleFont);
        paragraph.setAlignment(1); //设置文字居中 0靠左   1，居中     2，靠右
        paragraph.setIndentationLeft(12); //设置左缩进
//        paragraph.setIndentationRight(12); //设置右缩进
//        paragraph.setFirstLineIndent(24); //设置首行缩进
//        paragraph.setLeading(20f); //行间距
//        paragraph.setSpacingBefore(5f); //设置段落上空白
//        paragraph.setSpacingAfter(10f); //设置段落下空白

        // 段落
        Paragraph paragraph2 = new Paragraph("01 介绍您的创业初衷", headFont);
        paragraph2.setAlignment(0); //设置文字居中 0靠左   1，居中     2，靠右
        paragraph2.setIndentationLeft(12); //设置左缩进
//        paragraph2.setIndentationRight(12); //设置右缩进
//        paragraph2.setFirstLineIndent(24); //设置首行缩进
//        paragraph2.setLeading(20f); //行间距
//        paragraph2.setSpacingBefore(5f); //设置段落上空白
//        paragraph2.setSpacingAfter(10f); //设置段落下空白

        // 段落
        Paragraph paragraph3 = new Paragraph("创业初衷为xxxxxxx", textFont);
        paragraph3.setAlignment(0); //设置文字居中 0靠左   1，居中     2，靠右
        paragraph3.setIndentationLeft(12); //设置左缩进
//        paragraph3.setIndentationRight(12); //设置右缩进
//        paragraph3.setFirstLineIndent(24); //设置首行缩进
//        paragraph3.setLeading(20f); //行间距
//        paragraph3.setSpacingBefore(5f); //设置段落上空白
//        paragraph3.setSpacingAfter(10f); //设置段落下空白


        // 直线
        Paragraph p1 = new Paragraph();
        p1.add(new Chunk(new LineSeparator()));

        // 点线
        Paragraph p2 = new Paragraph();
        p2.add(new Chunk(new DottedLineSeparator()));

        // 超链接
        Anchor anchor = new Anchor("baidu");
        anchor.setReference("www.baidu.com");

        // 定位
        Anchor gotoP = new Anchor("goto");
        gotoP.setReference("#top");

        document.add(paragraph);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(anchor);
        document.add(p2);
        document.add(gotoP);
        document.add(p1);
    }

    public static void main(String args[]) throws Exception {
        List<PdfDto> pdfDtoList = new ArrayList<>();
        PdfDto pdfDto = new PdfDto();
        pdfDto.setText("正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的");
        pdfDto.setHead("正文小标题1是这样的");
        pdfDto.setTitle("路演演练演讲稿");
        pdfDtoList.add(pdfDto);
        pdfDto = new PdfDto();
        pdfDto.setText("正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的正文内容1是这样的");
        pdfDto.setHead("正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的正文小标题2是这样的");
        pdfDto.setTitle("路演演练演讲稿");
        pdfDtoList.add(pdfDto);
        createPdf(pdfDtoList);
    }

}
