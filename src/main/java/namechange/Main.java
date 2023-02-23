package namechange;

import namechange.excel.ExcelModel;
import namechange.excel.Read;
import org.apache.commons.collections4.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        createWindow();
    }

    public static void createWindow() {
        JFrame frame = new JFrame("图片转化");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI(frame);
        frame.setSize(560, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void createUI(final JFrame frame) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        JFileChooser onlyFileChooser = new JFileChooser();
        onlyFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        JFileChooser fileChooser = new JFileChooser();

        JButton selectImageFileButton = new JButton("选择图片文件夹");
        final JLabel selectImageFileButtonLabel = new JLabel();
        selectImageFileButton.addActionListener(e -> {
            int option = onlyFileChooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = onlyFileChooser.getSelectedFile();
                selectImageFileButtonLabel.setText("选择的文件是: " + file.getName());

            }
        });
        panel.add(selectImageFileButton);
        panel.add(selectImageFileButtonLabel);

        JButton selectExcelFileButton = new JButton("选择Excel表格");
        final JLabel selectExcelFileButtonLabel = new JLabel();
        selectExcelFileButton.addActionListener(e -> {
            int option = fileChooser.showOpenDialog(frame);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                selectExcelFileButtonLabel.setText("选择的文件是: " + file.getName());
            }
        });
        panel.add(selectExcelFileButton);
        panel.add(selectExcelFileButtonLabel);

        JButton startButton = new JButton("开始转化文档");
        panel.add(startButton);
        startButton.addActionListener(e -> {
            selectImageFileButton.setEnabled(false);
            selectExcelFileButton.setEnabled(false);
            startButton.setEnabled(false);

            File imageFile = onlyFileChooser.getSelectedFile();
            File excelFile = fileChooser.getSelectedFile();
            try {
                mainAction(excelFile.getPath(), imageFile.getPath());
                JOptionPane.showMessageDialog(null, "处理完成", "消息提示", JOptionPane.PLAIN_MESSAGE);    //消息对话框
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "处理失败，联系管理员", "消息提示", JOptionPane.ERROR_MESSAGE);
            }
        });
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }

    private static void mainAction(String excelPath, String folderPath) throws IOException {
        String fileName = "output.txt";
        FileWriter writer = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);


        Map<Integer, String> numberMap = getMap();
//        String excelPath = "C:\\Users\\bb135\\Desktop\\代发客户排序导单模板（吱吖）(1).xls";
//        String folderPath = "C:\\Users\\bb135\\Desktop\\徐方宇 2.7";
        Map<String, List<ExcelModel>> nameToExcelModel = Read.readExcel(excelPath);
        Map<String, List<File>> nameToFileList = Read.readImageFolder(folderPath);
        for (Map.Entry<String, List<File>> item : nameToFileList.entrySet()) {
            List<ExcelModel> excelModels = nameToExcelModel.get(item.getKey());
            if (CollectionUtils.isEmpty(excelModels)) {
//                System.out.println(item.getKey() + " 在表格中没有数据");
                bufferedWriter.write(item.getKey() + " 在表格中没有数据\n");
                continue;
            }
            List<File> value = item.getValue();
            if (excelModels.size() == 1) {
                ExcelModel excelModel = excelModels.get(0);
                for (int i = 0; i < value.size(); i++) {
                    File file = value.get(i);
                    String serialNumber = excelModel.getSerialNumber();
                    String name = excelModel.getName();
                    String[] split = excelModel.getSize().split("\\*");
                    int width = Integer.parseInt(split[0]);
                    int high = Integer.parseInt(split[1]);
                    String thickness = split[2];
                    String newName = serialNumber + "-" + name + " " + width / 10 + "-" + high / 10 + " " + thickness + "mm " + "第" + numberMap.get(i + 1) + "张";
                    if (value.size() == 1) {
                        newName = serialNumber + "-" + name + " " + width / 10 + "-" + high / 10 + " " + thickness + "mm";
                    }
                    renameTo(file, newName);
                }
            }
            if (excelModels.size() > 1) {
                Map<String, List<ExcelModel>> phoneToExcelModelMap = excelModels.stream().collect(Collectors.groupingBy(ExcelModel::getPhone));
                // 重名的情况
                if (phoneToExcelModelMap.size() > 1) {
                    for (File file : value) {
                        bufferedWriter.write("重名：" + item.getKey() + " 图片名: " + file.getName() + "\n");
//                        System.out.println("重名：" + item.getKey() + " 图片名: " + file.getName());
                    }
                    continue;
                }

                for (File file : value) {
                    // 一个人尺寸不一样的情况
                    bufferedWriter.write(item.getKey() + " 买了多张尺寸不同的 图片名： " + file.getName() + "\n");
//                    System.out.println(item.getKey() + " 买了多张尺寸不同的 图片名： " + file.getName());
                }
            }
        }
        bufferedWriter.close();
    }


    private static void renameTo(File oldFile, String newName) {
        String parent = oldFile.getParent();
        String newPath = parent + File.separator + newName + ".jpg";
        File newFile = new File(newPath);
        oldFile.renameTo(newFile);
    }

    private static Map<Integer, String> getMap() {
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
