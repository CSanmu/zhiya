package namechange.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Read {

    public static Map<String, List<ExcelModel>> readExcel(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            List<ExcelModel> excelModels = EasyExcel.read(inputStream).head(ExcelModel.class).sheet().headRowNumber(1).doReadSync();
            if(CollectionUtils.isEmpty(excelModels)){
                return new HashMap<>();
            }
            Map<String,List<ExcelModel>> nameToExcelModelList = new HashMap<>();
            for (ExcelModel excelModel : excelModels) {
                String name = excelModel.getName();
                List<ExcelModel> excelModelList = nameToExcelModelList.getOrDefault(name, new ArrayList<>());
                excelModelList.add(excelModel);
                nameToExcelModelList.put(name,excelModelList);
            }
            return nameToExcelModelList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static Map<String, List<File>> readImageFolder(String folderPath){
        // 创建一个Map对象来存储图像文件
        // 读取文件夹中的所有文件
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if(Objects.isNull(files)){
            return new HashMap<>();
        }
        // 遍历文件夹中的所有文件
        Map<String, List<File>> userNameToFileListMap = new HashMap<>();
        Map<String, File> fileNameToFileMap = Arrays.stream(files).filter(File::isFile).collect(Collectors.toMap(File::getName, t -> t));
        for (Map.Entry<String, File> item : fileNameToFileMap.entrySet()) {
            String[] s = item.getKey().split(" ");
            String name = s[0];
            List<File> fileList = userNameToFileListMap.getOrDefault(name, new ArrayList<>());
            fileList.add(item.getValue());
            userNameToFileListMap.put(name,fileList);
        }
        return userNameToFileListMap;
    }


    public static void main(String[] args) {
        System.out.println(JSONObject.toJSONString(readExcel("C:\\Users\\bb135\\Desktop\\代发客户排序导单模板（吱吖）(1).xls")));
        System.out.println(JSONObject.toJSONString(readImageFolder("C:\\Users\\bb135\\Desktop\\徐方宇 2.7")));
    }
}
