package namechange;

import namechange.excel.ExcelModel;
import namechange.excel.Read;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {
        Map<Integer, String> numberMap = getMap();
        String excelPath = "C:\\Users\\bb135\\Desktop\\代发客户排序导单模板（吱吖）(1).xls";
        String folderPath = "C:\\Users\\bb135\\Desktop\\徐方宇 2.7";
        Map<String, List<ExcelModel>> nameToExcelModel = Read.readExcel(excelPath);
        Map<String, List<File>> nameToFileList = Read.readImageFolder(folderPath);
        for (Map.Entry<String, List<File>> item : nameToFileList.entrySet()) {
            List<ExcelModel> excelModels = nameToExcelModel.get(item.getKey());
            List<File> value = item.getValue();
            if (excelModels.size() == 1) {
                ExcelModel excelModel = excelModels.get(0);
                for (int i = 1; i <= value.size(); i++) {
                    File file = value.get(i);
                    String serialNumber = excelModel.getSerialNumber();
                    String name = excelModel.getName();
                    String[] split = excelModel.getSize().split("/*");
                    int width = Integer.parseInt(split[0]);
                    int high = Integer.parseInt(split[1]);
                    String thickness = split[2];
                    String newName = serialNumber + "-" + name + " " + width / 10 + "-" + high / 10 + " " + thickness + "mm" + "第" + numberMap.get(i) + "张";
                    renameTo(file, newName);
                }
            }


//            Map<String, ExcelModel> sizeToCountMap = excelModels.stream().collect(Collectors.toMap(r -> {
//                String[] split = r.getSize().split("/*");
//                int width = Integer.parseInt(split[0]);
//                int high = Integer.parseInt(split[1]);
//                return width / 10 + "-" + high / 10;
//            }, t->t));
        }
    }


    public static void renameTo(File oldFile, String newName) {
        String parent = oldFile.getParent();
        String newPath = parent + File.separator + newName;
        File newFile = new File(newPath);
        newFile.renameTo(newFile);
    }

    public static Map<Integer, String> getMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "一");
        map.put(2, "二");
        map.put(3, "三");
        map.put(4, "四");
        map.put(5, "五");
        map.put(6, "六");
        map.put(7, "七");
        map.put(8, "八");
        map.put(9, "九");
        map.put(10, "十");
        map.put(11, "十一");
        map.put(12, "十二");
        map.put(13, "十三");
        map.put(14, "十四");
        map.put(15, "十五");
        map.put(16, "十六");
        map.put(17, "十七");
        map.put(18, "十八");
        map.put(19, "十九");
        map.put(20, "二十");
        map.put(21, "二十一");
        map.put(22, "二十二");
        map.put(23, "二十三");
        map.put(24, "二十四");
        map.put(25, "二十五");
        map.put(26, "二十六");
        map.put(27, "二十七");
        map.put(28, "二十八");
        map.put(29, "二十九");
        map.put(30, "三十");
        return map;
    }
}
