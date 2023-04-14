package photonamechange.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelModel {


    @ExcelProperty("买家旺旺")
    private String name;

    @ExcelProperty("订单编号")
    private String id;

    @ExcelProperty("销售属性")
    private String properties;
}
