import org.apache.poi.hwpf.HWPFDocument;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class App1 {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("/Users/hejie/Desktop/123.doc");
        HWPFDocument hwpfDocument = new HWPFDocument(inputStream);
        //以上Spire.Doc 生成的文件会自带警告信息，这里来删除Spire.Doc 的警告
        //hwpfDocument.delete() 该方法去掉文档指定长度的内容
//        hwpfDocument.delete(0,70);
        //输出word内容文件流，新输出路径位置
        OutputStream os=new FileOutputStream("/Users/hejie/Desktop/11/123.doc");
        try {
            hwpfDocument.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            hwpfDocument.close();
            os.close();
            inputStream.close();
        }
    }
}
