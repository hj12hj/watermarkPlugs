package com.hj.word;

import com.hj.core.AddWaterMark;
import com.hj.core.WaterMarkAttribute;
import com.hj.core.enums.WaveMarkMode;
import com.itextpdf.text.DocumentException;
import com.microsoft.schemas.office.office.CTLock;
import com.microsoft.schemas.vml.*;
import org.apache.commons.io.IOUtils;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * poi 实现 暂时没有添加图片水印
 */
@Deprecated
public class WordAddWaterMarkPoi implements AddWaterMark {


    /**
     * 属性
     */
    private WaterMarkAttribute waterMarkAttribute;


    private String customText; // 水印文字
    private String fontName = "宋体"; // word字体
    private String fontSize = "35pt"; // 字体大小
    private String fontColor = "#000000"; // 字体颜色
    private int widthPerWord = 20; // 一个字平均长度，单位pt，用于：计算文本占用的长度（文本总个数*单字长度）
    private String styleTop = "0"; // 与顶部的间距
    private String styleRotation = "-45"; // 文本旋转角度

    public WordAddWaterMarkPoi() {
    }

    public WordAddWaterMarkPoi(String customText) {
        customText = customText + repeatString(" ", 8); // 水印文字之间使用8个空格分隔
        this.customText = repeatString(customText, 1); // 一行水印重复水印文字次数
    }

    /**
     * 【核心方法】将输入流中的docx文档加载添加水印后输出到输出流中.
     *
     */
    public void makeSlopeWaterMark(String sourcePath,String targetPath,String customText) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(sourcePath));
        OutputStream outputStream = new FileOutputStream(new File(targetPath));
        customText = customText + repeatString(" ", 8); // 水印文字之间使用8个空格分隔
        this.customText = repeatString(customText, 1); // 一行水印重复水印文字次数


        Path tempFile = createTempFile(inputStream);
        if (tempFile == null) {
            return;
        }
        try (BufferedInputStream buffIn = new BufferedInputStream(Files.newInputStream(tempFile))) {
            XWPFDocument doc = loadDocXDocument(buffIn, outputStream);
            if (doc == null) {
                return;
            }
            // 遍历文档，添加水印
            for (int lineIndex = -10; lineIndex < 10; lineIndex++) {
                styleTop = 200 * lineIndex + " ";
                waterMarkDocXDocument(doc);
            }
            try {
                doc.write(outputStream); // 写出添加水印后的文档
            } finally {
                IOUtils.closeQuietly(doc);
            }
        } catch (Exception exception) {
            throw new RuntimeException("水印添加失败！");
        } finally {
            deleteFile(tempFile);
        }
    }

    /**
     * 为文档添加水印<br />
     *
     * @param doc 需要被处理的docx文档对象
     */
    private void waterMarkDocXDocument(XWPFDocument doc) {
        XWPFHeader header = doc.createHeader(HeaderFooterType.DEFAULT); // 如果之前已经创建过 DEFAULT 的Header，将会复用之
        int size = header.getParagraphs().size();
        if (size == 0) {
            header.createParagraph();
        }
        CTP ctp = header.getParagraphArray(0).getCTP();
        byte[] rsidr = doc.getDocument().getBody().getPArray(0).getRsidR();
        byte[] rsidrdefault = doc.getDocument().getBody().getPArray(0).getRsidRDefault();
        ctp.setRsidP(rsidr);
        ctp.setRsidRDefault(rsidrdefault);
        CTPPr ppr = ctp.addNewPPr();
        ppr.addNewPStyle().setVal("Header");
        // 开始加水印
        CTR ctr = ctp.addNewR();
        CTRPr ctrpr = ctr.addNewRPr();
        ctrpr.addNewNoProof();
        CTGroup group = CTGroup.Factory.newInstance();
        CTShapetype shapetype = group.addNewShapetype();
        CTTextPath shapeTypeTextPath = shapetype.addNewTextpath();
        shapeTypeTextPath.setOn(STTrueFalse.T);
        shapeTypeTextPath.setFitshape(STTrueFalse.T);
        CTLock lock = shapetype.addNewLock();
        lock.setExt(STExt.VIEW);
        CTShape shape = group.addNewShape();
        shape.setId("PowerPlusWaterMarkObject");
        shape.setSpid("_x0000_s102");
        shape.setType("#_x0000_t136");
        shape.setStyle(getShapeStyle()); // 设置形状样式（旋转，位置，相对路径等参数）
        shape.setFillcolor(fontColor);
        shape.setStroked(STTrueFalse.FALSE); // 字体设置为实心
        CTTextPath shapeTextPath = shape.addNewTextpath(); // 绘制文本的路径
        shapeTextPath.setStyle("font-family:" + fontName + ";font-size:" + fontSize); // 设置文本字体与大小
        shapeTextPath.setString(customText);
        CTPicture pict = ctr.addNewPict();
        pict.set(group);
    }

    // 构建Shape的样式参数
    private String getShapeStyle() {
        StringBuilder sb = new StringBuilder();
        sb.append("position: ").append("absolute"); // 文本path绘制的定位方式
        sb.append(";width: ").append(customText.length() * widthPerWord).append("pt"); // 计算文本占用的长度（文本总个数*单字长度）
        sb.append(";height: ").append("40pt"); // 字体高度
        sb.append(";z-index: ").append("-251654144");
        sb.append(";mso-wrap-edited: ").append("f");
        sb.append(";top: ").append(styleTop);
        sb.append(";mso-position-horizontal-relative: ").append("page");
        sb.append(";mso-position-vertical-relative: ").append("page");
        sb.append(";mso-position-vertical: ").append("center");
        sb.append(";mso-position-horizontal: ").append("center");
        sb.append(";rotation: ").append(styleRotation);
        return sb.toString();
    }

    // 加载docx格式的word文档
    private XWPFDocument loadDocXDocument(InputStream inputStream, OutputStream outputStream) {
        XWPFDocument doc;
        try {
            doc = new XWPFDocument(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("文档加载失败！！");
        }
        return doc;
    }

    /**
     * 创建临时文件.
     *
     * @param inputStream docx文档输入流
     */
    private Path createTempFile(InputStream inputStream) {
        Path tempFilePath;
        inputStream = (inputStream == null) ? new ByteArrayInputStream(new byte[0]) : inputStream; // 如果传入了null输入流，转换成空数组流
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        bufferedInputStream.mark(0); // 输入流头部打上Mark（方便重读）

        // 创建临时文件
        try {
            tempFilePath = Files.createTempFile("lanting", ".docx");
            // 向临时文件写入数据
            try (OutputStream tempout = Files.newOutputStream(tempFilePath)) {
                IOUtils.copy(bufferedInputStream, tempout);
            } catch (Exception e) { // 如果拷贝异常，删除临时文件
                deleteFile(tempFilePath);
            }
        } catch (Exception e) {
            // 这里表示创建临时文件失败
            tempFilePath = null;
        }
        return tempFilePath;
    }

    // 删除指定path的文件
    private void deleteFile(Path path) {
        if (path != null) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 将指定的字符串重复repeats次.
     */
    private String repeatString(String pattern, int repeats) {
        StringBuilder buffer = new StringBuilder(pattern.length() * repeats);
        Stream.generate(() -> pattern).limit(repeats).forEach(buffer::append);
        return new String(buffer);
    }

    @Override
    public void init() {

    }

    @Override
    public void transfer(String sourcePath, String targetPath, String waterMarkContent) throws DocumentException, IOException {
        makeSlopeWaterMark(sourcePath,targetPath,waterMarkContent);
    }





    @Override
    public void setWaveMarkMode(WaveMarkMode waveMarkMode) {

    }

    @Override
    public void setPicPath(String picPath) {

    }

    @Override
    public void setWaterMarkAttribute(WaterMarkAttribute waterMarkAttribute) {

    }
}
