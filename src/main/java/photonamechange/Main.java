package photonamechange;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.collections4.CollectionUtils;
import photonamechange.excel.ExcelModel;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static List<ExcelModel> readExcel(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            List<ExcelModel> excelModels = EasyExcel.read(inputStream).head(ExcelModel.class).sheet().headRowNumber(1).doReadSync();
            if (CollectionUtils.isEmpty(excelModels)) {
                return new ArrayList<>();
            }
            return excelModels;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static Map<String, File> readImageFolder(String folderPath) {
        // 创建一个Map对象来存储图像文件
        // 读取文件夹中的所有文件
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (Objects.isNull(files)) {
            return new HashMap<>();
        }
        return Arrays.stream(files).filter(File::isFile).collect(Collectors.toMap(item -> item.getName().replaceFirst("[.][^.]+$", ""), t -> t));
    }


    public static void main(String[] args) throws IOException {
        List<ExcelModel> excelModels = readExcel("/Users/chenzengsen09222/Desktop/副本模拟表格(1)(1).xls");
        Map<String, File> nameToPhoto = readImageFolder("/Users/chenzengsen09222/Desktop/1");
        String fileName = "output.txt";
        FileWriter writer = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        excelModels.forEach(item -> {
            try {
                System.out.println(item.getProperties());
                String[] properties = item.getProperties().split(" ");
                String photoName = properties[0];
                String size = properties[1];
                String hd = properties[2];
                String newName = String.format("%s %s %s", item.getName(), size, hd);
                System.out.println(newName);
                File oldFile = nameToPhoto.get(photoName);
                if (Objects.isNull(oldFile) || !oldFile.exists()) {
                    bufferedWriter.write("订单编号:" + item.getId() + " 文件中没有这张图\n");
                    return;
                }
                String parent = oldFile.getParent();
                String newPath = parent + File.separator + newName + ".jpg";
                oldFile.renameTo(new File(newPath));
            } catch (ArrayIndexOutOfBoundsException e) {
                try {
                    bufferedWriter.write("订单编号:" + item.getId() + " 格式错误\n");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bufferedWriter.close();
    }

}
