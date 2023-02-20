package namechange.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelModel {

    @ExcelProperty("序号")
    private String serialNumber;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("电话")
    private String phone;

    @ExcelProperty("地址")
    private String address;

    @ExcelProperty("尺寸")
    private String size;

    @ExcelProperty("数量")
    private String number;
}
