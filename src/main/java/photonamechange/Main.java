package photonamechange;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.collections4.CollectionUtils;
import photonamechange.excel.ExcelModel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
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
        createWindow();
    }

    public static void createWindow() {
        JFrame frame = new JFrame("图片重命名");
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

        JButton startButton = new JButton("开始重命名");
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

    private static void mainAction(String excelPath, String filePath) throws IOException {
        List<ExcelModel> excelModels = readExcel(excelPath);
        Map<String, File> nameToPhoto = readImageFolder(filePath);
        FileWriter writer = new FileWriter("output.txt");
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
