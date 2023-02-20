package namechange.excel;

import com.alibaba.excel.EasyExcel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class Read {

    public static void read(String path) {

        try {
            InputStream inputStream = new FileInputStream(path);
            List<ExcelModel> modelList = EasyExcel.read(inputStream).head(ExcelModel.class).sheet().headRowNumber(1).doReadSync();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Read.read("/Users/chenzengsen09222/Desktop/excel.xls");
    }
}
